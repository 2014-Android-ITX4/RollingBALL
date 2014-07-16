package com.example.rollingball.app;

import com.hackoeur.jglm.Vec3;

import java.util.InputMismatchException;
import java.util.LinkedList;

public class Scene implements IUpdatable, IDrawable
{
  private Vec3 _gravity_in_m_per_s_s = new Vec3( 0.0f, -9.8f, 0.0f);

  public LinkedList<GameObject> game_objects  = new LinkedList< GameObject >( );
  public boolean                to_exit       = false;
  public Camera                 camera        = new Camera( this );
  public Lighting               lighting      = new Lighting();
  public InputManager           input_manager = new InputManager( this );
  public SceneManager           scene_manager = null;

  protected Message[] _massages;

  public Scene( final SceneManager scene_manager_ )
  {
    scene_manager = scene_manager_;
  }

  void push( GameObject game_object )
  {
    game_objects.push( game_object );
  }

  void message( Message message )
  {

  }

  @Override
  public void update( final float delta_time_in_seconds )
  {
    input_manager.update( delta_time_in_seconds );
    camera.update( delta_time_in_seconds );

    for ( GameObject g : game_objects )
    {
      g.update( delta_time_in_seconds );
      g.effect_gravity( _gravity_in_m_per_s_s );
    }
  }

  @Override
  public void draw()
  {
    lighting.draw();

    for ( GameObject g : game_objects )
      g.draw();
  }

}
