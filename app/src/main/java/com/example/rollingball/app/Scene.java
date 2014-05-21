package com.example.rollingball.app;

import android.hardware.input.InputManager;

import java.util.InputMismatchException;
import java.util.LinkedList;

/**
 * Created by sakamoto on 2014/05/14.
 */
public class Scene implements IUpdatetable
{
  public LinkedList<GameObject> game_objects;
  public Camera camera;
  public InputManager input_manager;
  public SceneManager scene_manager;
  protected Message[] _massages;
  public boolean to_exit;

  Scene(){
    camera = new Camera();
    input_manager = new InputManager();
  }

  Scene( SceneManager s )
  {
    scene_manager = s;
  }

  void push( GameObject game_object ){

  }

  void message( Message message ){

  }

  public SceneManager getScene_manager(){
    return scene_manager;
  }

  public void update( long delta_time_in_ns ){
      for ( GameObject g : game_objects ) update(delta_time_in_ns);
  }
}
