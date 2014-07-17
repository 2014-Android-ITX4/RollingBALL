package com.example.rollingball.app;

import com.hackoeur.jglm.Vec3;
import java.util.ArrayList;

public class RigidBodyGameObject extends GameObject
{
  public float mass = 1.0f;             //質量
  public Vec3 velocity = new Vec3( 0, 0, 0 );          //速度
  public ArrayList<Vec3> forces = new ArrayList< Vec3 >( ); //力
  public ArrayList< BoundingSphere > collision_boundings = new ArrayList< BoundingSphere>( );

  public RigidBodyGameObject( Scene scene )
  { super( scene );}

  @Override
  public void update( final float delta_time_in_seconds )
  {
    Vec3 force_sum = new Vec3( 0.0f, 0.0f, 0.0f );

    for( Vec3 f: forces )
      force_sum = force_sum.add( f );

    forces.clear();

    Vec3 acceleration = force_sum.multiply( 1.0f / mass );
    velocity = velocity.add( acceleration.multiply( delta_time_in_seconds ) );
    position = position.add( velocity.multiply( delta_time_in_seconds ) );

    if ( StageScene.class.isInstance( _scene ) )
    {
      StageScene s = (StageScene)_scene;
      if ( position.getY() < s.death_height() )
        position = new Vec3( position.getX(), s.death_height(), position.getZ() );
    }

    super.update( delta_time_in_seconds );
  }

  @Override
  public void effect_gravity( final Vec3 g_in_m_per_s_s )
  {
    this.forces.add( g_in_m_per_s_s );
  }
}