package com.example.rollingball.app;

import com.hackoeur.jglm.Vec3;
import java.util.ArrayList;
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
    _update_collision();
  }

  @Override
  public void draw()
  {
    lighting.draw();

    for ( GameObject g : game_objects )
      g.draw();
  }

  public void on_resume()
  {
    for ( GameObject g : game_objects )
      g.on_resume();
  }

  protected void _update_collision()
  {
   // LinkedList<GameObject> _object = new LinkedList< GameObject >();
    for ( int na = 0; na < game_objects.size(); ++na )
      for ( int nb = na + 1; nb < game_objects.size(); ++nb )
        _collision(game_objects.get( na ),game_objects.get( nb ));

  }

  protected void _collision(GameObject a, GameObject b)
  {
    if ( a instanceof RigidBodyGameObject && b instanceof RigidBodyGameObject )
      _collision_body_body( ( RigidBodyGameObject )a, ( RigidBodyGameObject )b );
    else if ( a instanceof RigidBodyGameObject && b instanceof FieldGameObject )
      _collision_body_field( ( RigidBodyGameObject )a, ( FieldGameObject )b);
    else if ( b instanceof RigidBodyGameObject && a instanceof FieldGameObject )
      _collision_body_field( ( RigidBodyGameObject )b, ( FieldGameObject )a);

  }

  protected void _collision_body_body(RigidBodyGameObject a,RigidBodyGameObject b)
  {
    for ( float ar : a.collision_radiuses )
      for ( float br : b.collision_radiuses )
      {
        Vec3 delta = new Vec3( b.position.subtract( a.position ) );
        float distance = ( float ) Math.sqrt(
          Math.pow( delta.getX(), 2 ) + Math.pow( delta.getY(), 2 ) + Math.pow( delta.getZ(), 2 )
        );
        if ( distance <= ar + br )
        {
          // 当たり処理
          a.forces.add( b.forces.get( b.forces.size() - 1 ) );
          b.forces.add( a.forces.get( a.forces.size() - 2 ) );

        }
      }

  }

  protected void _collision_body_field( RigidBodyGameObject a, FieldGameObject b )
  {
    for ( float ar : a.collision_radiuses )
    {
      Vec3 reflection_x = new Vec3( a.velocity.getX() * 2, 0, 0 );
      Vec3 reflection_y = new Vec3( 0, ar - a.position.getY(), 0 );
      Vec3 reflection_z = new Vec3( 0, 0, a.velocity.getZ() * 2 );
      int position_x = ( int ) b.position.getX();
      int position_z = ( int ) b.position.getZ();
      float a_dot_b = a.velocity.dot( b.position );
      double theta = Math.acos( a_dot_b );

      if ( a.position.getY() < ar )
        a.position.add( reflection_y );

      if ( Math.abs( theta ) > ( float ) ( Math.PI * 0.5 ) )
      {
        if ( b.field_planes.get( position_x + 1 ).get( position_z ) == 1 || b.field_planes.get(
          position_x - 1
        ).get( position_z ) == 1 )
        {
          a.velocity.subtract( reflection_z );

        }
        else if ( b.field_planes.get( position_x ).get( position_z + 1 ) == 1 || b.field_planes.get(
          position_x
        ).get( position_z - 1 ) == 1 )
        {
          a.velocity.subtract( reflection_x );
        }
      }
      else
      {

      }
    }



  }


}
