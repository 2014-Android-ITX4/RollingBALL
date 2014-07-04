package com.example.rollingball.app;

import android.content.Context;
import android.opengl.GLSurfaceView;

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

}
