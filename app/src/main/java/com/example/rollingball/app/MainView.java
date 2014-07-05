package com.example.rollingball.app;

import android.content.Context;
import android.opengl.GLSurfaceView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainView extends GLSurfaceView
{
  public MainRenderer renderer;
  public SceneManager scene_manager;
  public MainActivity activity;
  public int screen_width;
  public int screen_height;

  public MainView( Context context, MainActivity arg_activity )
  {
    super( context );

    activity = arg_activity;

    this.setEGLContextClientVersion( 2 );
    renderer = new MainRenderer( this );
    this.setRenderer( renderer );

    scene_manager = new SceneManager( this );
  }

  public String load_text_from_raw_resource( int resource_id )
  {
    InputStream s = getResources().openRawResource( resource_id );
    InputStreamReader sr = new InputStreamReader( s );

    final int buffer_size = 64 * 1024;

    char[] char_buffer = new char[ buffer_size ];
    int readed_size = 0;

    try
    { readed_size = sr.read( char_buffer, 0, buffer_size ); }
    catch ( IOException e )
    { e.printStackTrace(); }

    return String.valueOf( char_buffer, 0, readed_size );
  }

}
