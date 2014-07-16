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
  private MainView _main_view = null;

  private long _before_time_in_ns = 0;

  private int _screen_width  = 0;
  private int _screen_height = 0;

  private float _field_of_view = (float)( Math.PI / 3.0 );
  private float _aspect_ratio  = 1.0f;
  private float _near_clip     = 1.0e-3f;
  private float _far_clip      = 1.0e+3f;

  private int _program = 0;

  public MainRenderer( final MainView main_view )
  { _main_view = main_view; }

  @Override
  public void onDrawFrame(GL10 gl){

    if( _before_time_in_ns == 0 )
      _before_time_in_ns = System.nanoTime();

    final long delta_time_in_ns = System.nanoTime() - _before_time_in_ns;

    _main_view.scene_manager.update( 1.0e-9f * (float)delta_time_in_ns );

    GLES20.glClear( GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT );

    _main_view.scene_manager.draw();

    _before_time_in_ns = System.nanoTime();
  }

  @Override
  public void onSurfaceChanged( final GL10 gl10, final int width, final int height )
  {
    Log.d( "MainRenderer","Call onSurfaceChanged" );
    Log.d( "change screen width" , String.valueOf( width ) );
    Log.d( "change screen height", String.valueOf( height ) );

    _screen_width  = width;
    _screen_height = height;
    _aspect_ratio  = (float)width / (float) height;

    GLES20.glViewport( 0, 0, width, height );

    uniform_projection_transformation( _field_of_view, _aspect_ratio, _near_clip, _far_clip );
  }

  @Override
  public void onSurfaceCreated( final GL10 gl10, final EGLConfig eglConfig )
  {
    Log.d( "MainRenderer","Call onSurfaceCreated" );
    _program = create_program_from_sources
      ( _main_view.load_text_from_raw_resource( R.raw.shader_default_vertex )
      , _main_view.load_text_from_raw_resource( R.raw.shader_default_fragment )
      );

    GLES20.glUseProgram( _program );

    GLES20.glEnableVertexAttribArray( GLES20.glGetAttribLocation( _program, "position" ) );
    GLES20.glEnableVertexAttribArray( GLES20.glGetAttribLocation( _program, "normal"   ) );
    // TODO: テクスチャーを使うようになったらどうぞ。
    //GLES20.glEnableVertexAttribArray( GLES20.glGetAttribLocation( _program, "texcoord" ) );

    // デプスバッファの有効化
    GLES20.glEnable( GLES20.GL_DEPTH_TEST );
    GLES20.glDepthFunc( GLES20.GL_LEQUAL );
    GLES20.glDepthMask( true );

    // カリング
    GLES20.glEnable( GLES20.GL_CULL_FACE );
    GLES20.glCullFace( GLES20.GL_BACK );

    uniform_projection_transformation( _field_of_view, _aspect_ratio, _near_clip, _far_clip );

    // ポーズフラグがtrueになっていたら各ModelDataの頂点・インデックスを再読込させる
    if ( _main_view.activity.pause_flag == true )
    {
      _main_view.scene_manager.on_resume();
      _main_view.activity.pause_flag = false;
    }

  }

  public int screen_width( )
  {
    return _screen_width;
  }

  public int screen_height( )
  {
   return _screen_height;
  }

  private void uniform_projection_transformation( float field_of_view_in_rad, float aspect_ratio, float near_clip, float far_clip )
  {
    Mat4 projection = Matrices.perspective( (float)Math.toDegrees( field_of_view_in_rad ), aspect_ratio, near_clip, far_clip );
    int location_of_projection_transformation = GLES20.glGetUniformLocation( _program , "projection_transformation" );
    GLES20.glUniformMatrix4fv( location_of_projection_transformation, 1, false, projection.getBuffer() );
  }

  // String ( shader source code ) --> int ( shader id )
  private static int create_shader( String shader_source_code, int shader_type )
  {
    int shader_id = GLES20.glCreateShader( shader_type );

    if( shader_id == 0 )
      throw new RuntimeException();

    GLES20.glShaderSource( shader_id, shader_source_code );
    GLES20.glCompileShader( shader_id );

    final int[] vertex_shader_result = new int[ 1 ];
    GLES20.glGetShaderiv( shader_id, GLES20.GL_COMPILE_STATUS, vertex_shader_result, 0 );
    if ( vertex_shader_result[ 0 ] == 0 )
      throw new RuntimeException( GLES20.glGetShaderInfoLog( shader_id ) );

    return shader_id;
  }

  private static int create_vertex_shader( String shader_source_code )
  { return create_shader( shader_source_code, GLES20.GL_VERTEX_SHADER ); }

  private static int create_fragment_shader( String shader_source_code )
  { return create_shader( shader_source_code, GLES20.GL_FRAGMENT_SHADER ); }

  private static int create_program( int vertex_shader_id, int fragment_shader_id )
  {
    // shader program
    int program = GLES20.glCreateProgram();
    if ( program == 0 )
      throw new RuntimeException();

    GLES20.glAttachShader( program, vertex_shader_id   );
    GLES20.glAttachShader( program, fragment_shader_id );

    GLES20.glLinkProgram( program );
    final int[] linkStatus = new int[1];
    GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
    if (linkStatus[0] != GLES20.GL_TRUE)
      throw new RuntimeException( GLES20.glGetProgramInfoLog( program ) );

    return program;
  }

  private static int create_program_with_delete_shader( int vertex_shader_id, int fragment_shader_id )
  {
    int program = create_program( vertex_shader_id, fragment_shader_id );

    GLES20.glDeleteShader( vertex_shader_id   );
    GLES20.glDeleteShader( fragment_shader_id );

    return program;
  }

  private static int create_program_from_sources( String vertex_shader_source, String fragment_shader_source )
  {
    int vertex_shader_id   = create_vertex_shader( vertex_shader_source );
    int fragment_shader_id = create_fragment_shader( fragment_shader_source );

    return create_program_with_delete_shader( vertex_shader_id, fragment_shader_id );
  }
}