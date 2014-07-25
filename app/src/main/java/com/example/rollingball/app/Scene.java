package com.example.rollingball.app;

import com.hackoeur.jglm.Vec3;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.LinkedList;

public class Scene implements IUpdatable, IDrawable
{
  private Vec3 _gravity_in_m_per_s_s = new Vec3( 0.0f, -9.8f, 0.0f );

  public LinkedList< GameObject > game_objects = new LinkedList< GameObject >();
  public boolean to_exit = false;
  public Camera camera = new Camera( this );
  public Lighting lighting = new Lighting();
  public InputManager input_manager = new InputManager( this );
  public SceneManager scene_manager = null;


  // GC対策のため移動したローカル変数
  BoundingSphere ab;
  private int _field_x;
  private int _field_z;
  private float _field_y;
  private Vec3 _floor_position;

  private int _field_xm;
  private int _field_zm;

  private Vec3 _wall_position;
  private Vec3 _wall_normal;

  private final Vec3 _WALL_PZZ = new Vec3( 1.0f, 0.0f, 0.0f );
  private final Vec3 _WALL_MZZ = new Vec3( -1.0f, 0.0f, 0.0f );
  private final Vec3 _WALL_ZZP = new Vec3( 0.0f, 0.0f, +1.0f );
  private final Vec3 _WALL_ZZM = new Vec3( 0.0f, 0.0f, -1.0f );


  private float _distance;


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

    for ( int i = 0; i < game_objects.size(); i++ )
    {
      game_objects.get( i ).update( delta_time_in_seconds );
      game_objects.get( i ).effect_gravity( _gravity_in_m_per_s_s );
    }
  }

  @Override
  public void draw()
  {
    lighting.draw();

    for ( int i = 0; i < game_objects.size(); i++ )
      game_objects.get( i ).draw();
  }

  public void on_resume()
  {
    for ( int i = 0; i < game_objects.size(); i++ )
      game_objects.get( i ).on_resume();
  }
}