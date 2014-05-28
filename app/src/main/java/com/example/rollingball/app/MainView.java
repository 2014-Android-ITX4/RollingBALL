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
    _renderer = new MainRenderer( this );
    activity = arg_activity;
  }

}
