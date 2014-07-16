package com.example.rollingball.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.view.View;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainView extends GLSurfaceView
{
  public MainRenderer renderer;
  public SceneManager scene_manager;
  public MainActivity activity;

  public MainView( Context context, MainActivity arg_activity )
  {
    super( context );

    setSystemUiVisibility
      ( View.SYSTEM_UI_FLAG_IMMERSIVE
      | View.SYSTEM_UI_FLAG_FULLSCREEN
      | View.SYSTEM_UI_FLAG_IMMERSIVE
      );

    activity = arg_activity;

    this.setEGLContextClientVersion( 2 );
    renderer = new MainRenderer( this );
    setRenderer( renderer );

    scene_manager = new SceneManager( this );
  }

  public Bitmap load_bitmap_from_asset( String path )
  {
    Bitmap bitmap = null;

    try
    {
      bitmap = BitmapFactory.decodeStream( getResources().getAssets().open( path ) );
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }

    return bitmap;
  }

  public String load_text_from_raw_resource( int resource_id )
  {
    InputStream s = getResources().openRawResource( resource_id );
    InputStreamReader sr = new InputStreamReader( s );

    final int buffer_size = 64 * 1024;

    char[] char_buffer = new char[ buffer_size ];
    int read_size = 0;

    try
    { read_size = sr.read( char_buffer, 0, buffer_size ); }
    catch ( IOException e )
    { e.printStackTrace(); }

    return String.valueOf( char_buffer, 0, read_size );
  }

  public int screen_width()
  { return renderer.screen_width(); }

  public int screen_height()
  { return renderer.screen_height(); }

}
