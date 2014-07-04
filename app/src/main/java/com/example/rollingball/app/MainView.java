package com.example.rollingball.app;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * Created by sakamoto on 2014/05/14.
 */
public class MainView extends GLSurfaceView
{
  private MainRenderer _renderer;
  public SceneManager scene_manager;
  public MainActivity activity;
  public int screen_width;
  public int screen_height;

  public MainView( Context context, MainActivity arg_activity )
  {
    super( context );

    activity = arg_activity;
    scene_manager = new SceneManager( this );

    this.setEGLContextClientVersion( 2 );
    _renderer = new MainRenderer( this );
    this.setRenderer( _renderer );
  }

}
