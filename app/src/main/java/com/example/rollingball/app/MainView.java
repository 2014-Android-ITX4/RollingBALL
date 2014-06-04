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

  public MainView( Context context, MainActivity arg_activity )
  {
    super(context);
    this.setEGLContextClientVersion( 2 );
    _renderer = new MainRenderer( this );
    this.setRenderer( _renderer );
    activity = arg_activity;
  }

}
