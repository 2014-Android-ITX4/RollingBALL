package com.example.rollingball.app;

import com.hackoeur.jglm.Vec3;

import java.util.ArrayList;
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
}

  void push( GameObject game_object ){

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

    for ( GameObject g : game_objects )
      g.update( delta_time_in_ns );

    input_manager.update( delta_time_in_ns );

    _update_collision();
  }

  @Override
  public void draw()
  {

  }

  protected void _update_collision()
  {
    LinkedList<GameObject> _object = new LinkedList< GameObject >();
    for ( int na = 0; na < _object.size(); ++na )
      for ( int nb = na + 1; nb < _object.size(); ++nb )
        _collision(_object.get( na ),_object.get( nb ));

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
    Vec3 reflection_x = new Vec3( a.velocity.getX() * 2, 0, 0 );
    Vec3 reflection_z = new Vec3( 0, 0, a.velocity.getZ() * 2 );
    int position_x = (int)b.position.getX();
    int position_z = (int)b.position.getZ();
    float a_dot_b = a.velocity.dot( b.position );
    double theta = Math.acos( a_dot_b );

    if ( Math.abs( theta ) > ( float )( Math.PI * 0.5 ) ){
      if ( b.field_planes.get( position_x + 1 ).get( position_z ) == 1 || b.field_planes.get( position_x - 1 ).get( position_z ) == 1 ){
        a.velocity.subtract( reflection_z );
      }else if ( b.field_planes.get( position_x ).get( position_z + 1 ) == 1 || b.field_planes.get( position_x ).get( position_z - 1) == 1 ){
        a.velocity.subtract( reflection_x );
      }
    }else{

    }


  }


}
