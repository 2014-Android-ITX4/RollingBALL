package com.example.rollingball.app;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

class MainRenderer implements GLSurfaceView.Renderer
{
  private MainView _main_view;
  private long _before_time_in_ns;

  public MainRenderer( final MainView main_view )
  { _main_view = main_view; }

  @Override
  public void onDrawFrame(GL10 gl){
    final long delta_time_in_ns = System.nanoTime() - _before_time_in_ns;

    _main_view.scene_manager.update( delta_time_in_ns );

    GLES20.glClearColor( 0.9f, 0.9f, 1.0f, 1.0f );
    GLES20.glClear( GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT );

    _main_view.scene_manager.draw();

    _before_time_in_ns = System.nanoTime();
  }

  @Override
  public void onSurfaceChanged( final GL10 gl10, final int width, final int height )
  {
    GLES20.glViewport( 0, 0, width, height );
  }

  @Override
  public void onSurfaceCreated( final GL10 gl10, final EGLConfig eglConfig )
  {
    int vertex_shader = GLES20.glCreateShader( GLES20.GL_VERTEX_SHADER );

    if( vertex_shader == 0 )
      throw new RuntimeException();

    GLES20.glShaderSource
      ( vertex_shader
        , "#version 100\n"
          + "attribute vec4 position;\n"
          + "attribute vec2 texcoord;\n"
          + "attribute vec3 normal;\n"
          + "varying vec2 var_texcoord;\n"
          + "varying vec3 var_normal;\n"
          + "uniform mat4 world_view_transformation;\n"
          + "void main()\n"
          + "{\n"
          + "  gl_Position = world_view_transformation * position;\n"
          + "  var_texcoord = texcoord;\n"
          + "  var_normal = normal;\n"
          + "}\n"
      );
    GLES20.glCompileShader( vertex_shader );

    final int[] vertex_shader_result = new int[ 1 ];
    GLES20.glGetShaderiv( vertex_shader, GLES20.GL_COMPILE_STATUS, vertex_shader_result, 0 );
    if ( vertex_shader_result[ 0 ] == 0 )
      throw new RuntimeException( GLES20.glGetShaderInfoLog( vertex_shader ) );

    // fragment shader
    int fragment_shader = GLES20.glCreateShader( GLES20.GL_FRAGMENT_SHADER );

    if ( fragment_shader == 0)
      throw new RuntimeException();

    GLES20.glShaderSource
      ( fragment_shader
        , "#version 100\n"
          + "#ifdef GL_FRAGMENT_PRECISION_HIGH\n"
          + "  precision highp float;\n"
          + "#else\n"
          + "  precision mediump float;\n"
          + "#endif\n"
          + "varying vec2 var_texcoord;\n"
          + "varying vec3 var_normal;\n"
          + "uniform sampler2D sampler;\n"
          + "uniform vec3 diffuse;\n"
          + "uniform vec3 ambient;\n"
          + "uniform vec3 specular;\n"
          + "uniform vec3 emisive;\n"
          + "uniform float transparent;\n"

          + "bool is_nan( float );\n"

          + "void main()\n"
          + "{\n"
          + "  gl_FragColor = is_nan( var_texcoord.x ) \n"
          + "    ? vec4( diffuse, 1.0 )\n"
          + "    : texture2D( texture, var_texcoord );\n"
          + "  gl_FragColor.a *= transparent;\n"
          + "}\n"

          + "bool is_nan( float val )\n"
          + "{\n"
          + "  return (val <= 0.0 || 0.0 <= val) ? false : true;\n"
          + "}\n"
      );
    GLES20.glCompileShader( fragment_shader );

    final int[] fragment_shader_result = new int[ 1 ];
    GLES20.glGetShaderiv( fragment_shader, GLES20.GL_COMPILE_STATUS, fragment_shader_result, 0 );
    if ( fragment_shader_result[ 0 ] == 0 )
      throw new RuntimeException( GLES20.glGetShaderInfoLog( fragment_shader ) );

    // shader program
    int program = GLES20.glCreateProgram();
    if ( program == 0 )
      throw new RuntimeException();

    GLES20.glAttachShader( program, vertex_shader );
    GLES20.glAttachShader( program, fragment_shader );

    GLES20.glLinkProgram( program );
    final int[] linkStatus = new int[1];
    GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
    if (linkStatus[0] != GLES20.GL_TRUE)
      throw new RuntimeException( GLES20.glGetProgramInfoLog( program ) );

    GLES20.glUseProgram( program );

    // デプスバッファの有効化
    GLES20.glEnable( GLES20.GL_DEPTH_TEST );

  }
}