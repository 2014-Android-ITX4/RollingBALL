package com.example.rollingball.app;

import java.util.InputMismatchException;
import java.util.LinkedList;

public class Scene implements IUpdatable, IDrawable
{
  public LinkedList<GameObject> game_objects  = new LinkedList< GameObject >( );
  public boolean                to_exit       = false;
  public Camera                 camera        = new Camera( this );
  public InputManager           input_manager = new InputManager( this );
  public SceneManager           scene_manager = null;

  protected Message[] _massages;

  Scene( SceneManager scene_manager )
  {
    scene_manager = scene_manager;
  }

  void push( GameObject game_object )
  {
    game_objects.push( game_object );
  }

  void message( Message message )
  {

  }

  public SceneManager getScene_manager(){
    return scene_manager;
  }

  @Override
  public void update( final float delta_time_in_seconds )
  {
    input_manager.update( delta_time_in_seconds );
    camera.update( delta_time_in_seconds );

    for ( GameObject g : game_objects )
      g.update( delta_time_in_seconds );
  }

  @Override
  public void draw()
  {
    for ( GameObject g : game_objects )
      g.draw();
  }

}
