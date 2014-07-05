package com.example.rollingball.app;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Matrices;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

class MainRenderer implements GLSurfaceView.Renderer
{
  private MainView _main_view;
  private long _before_time_in_ns;
  private int screen_width;
  private int screen_height;

  int program;

  public MainRenderer( final MainView main_view )
  { _main_view = main_view; }

  @Override
  public void onDrawFrame(GL10 gl){

    if( _before_time_in_ns == 0 )
      _before_time_in_ns = System.nanoTime();

    final long delta_time_in_ns = System.nanoTime() - _before_time_in_ns;

    _main_view.scene_manager.update( delta_time_in_ns );

    GLES20.glClear( GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT );

    _main_view.scene_manager.draw();

    _before_time_in_ns = System.nanoTime();
  }

  @Override
  public void onSurfaceChanged( final GL10 gl10, final int width, final int height )
  {
    screen_width = width;
    screen_height = height;
    setScreenW( screen_width );
    setScreenH( screen_height );
    Log.d( "size", "width："+screen_width );
    Log.d( "size", "height："+screen_height );
    GLES20.glViewport( 0, 0, width, height );
  }

  @Override
  public void onSurfaceCreated( final GL10 gl10, final EGLConfig eglConfig )
  {
    int vertex_shader = GLES20.glCreateShader( GLES20.GL_VERTEX_SHADER );

    if( vertex_shader == 0 )
      throw new RuntimeException();

    GLES20.glShaderSource( vertex_shader, _main_view.load_text_from_raw_resource( R.raw.shader_default_vertex ) );
    GLES20.glCompileShader( vertex_shader );

    final int[] vertex_shader_result = new int[ 1 ];
    GLES20.glGetShaderiv( vertex_shader, GLES20.GL_COMPILE_STATUS, vertex_shader_result, 0 );
    if ( vertex_shader_result[ 0 ] == 0 )
      throw new RuntimeException( GLES20.glGetShaderInfoLog( vertex_shader ) );

    // fragment shader
    int fragment_shader = GLES20.glCreateShader( GLES20.GL_FRAGMENT_SHADER );

    if ( fragment_shader == 0)
      throw new RuntimeException();

    GLES20.glShaderSource( fragment_shader, _main_view.load_text_from_raw_resource( R.raw.shader_default_fragment ));
    GLES20.glCompileShader( fragment_shader );

    final int[] fragment_shader_result = new int[ 1 ];
    GLES20.glGetShaderiv( fragment_shader, GLES20.GL_COMPILE_STATUS, fragment_shader_result, 0 );
    if ( fragment_shader_result[ 0 ] == 0 )
      throw new RuntimeException( GLES20.glGetShaderInfoLog( fragment_shader ) );

    // shader program
    program = GLES20.glCreateProgram();
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

    GLES20.glEnableVertexAttribArray( GLES20.glGetAttribLocation( program, "position" ) );
    // TODO: テクスチャーを使うようになったらどうぞ。
    //GLES20.glEnableVertexAttribArray( GLES20.glGetAttribLocation( program, "texcoord" ) );

    // デプスバッファの有効化
    GLES20.glEnable( GLES20.GL_DEPTH_TEST );

    Mat4 projection = Matrices.perspective( 60, 16.0f/9.0f, 0.001f, 1000 );
    int location_of_projection_transformation = GLES20.glGetUniformLocation( program , "projection_transformation" );
    GLES20.glUniformMatrix4fv( location_of_projection_transformation, 1, false, projection.getBuffer() );
  }

  public void setScreenW(int screen_width)
  {
    _main_view.screen_width = screen_width;
  }

  public void setScreenH(int screen_height)
  {
    _main_view.screen_height = screen_height;
  }

}