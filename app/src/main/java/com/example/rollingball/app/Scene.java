package com.example.rollingball.app;

import java.util.InputMismatchException;
import java.util.LinkedList;

public class Scene implements IUpdatable, IDrawable
{
  public LinkedList<GameObject> game_objects;
  public Camera camera;
  public InputManager input_manager;
  public SceneManager scene_manager;
  protected Message[] _massages;
  public boolean to_exit;

  Scene( SceneManager s )
  {
    scene_manager = s;
    game_objects = new LinkedList< GameObject >();
    camera = new Camera();
    input_manager = new InputManager( this );
    to_exit = false;
}

  void push( GameObject game_object )
  {
    game_objects.push( game_object );
  }

  void message( Message message ){

  }

  public SceneManager getScene_manager(){
    return scene_manager;
  }

  @Override
  public void update( long delta_time_in_ns )
  {
    input_manager.update( delta_time_in_ns );
    camera.update( delta_time_in_ns );

    for ( GameObject g : game_objects )
      g.update( delta_time_in_ns );
  }

  @Override
  public void draw()
  {
    for ( GameObject g : game_objects )
      g.draw();
  }

}
