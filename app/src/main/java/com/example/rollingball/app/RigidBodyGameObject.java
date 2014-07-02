package com.example.rollingball.app;

import com.hackoeur.jglm.Vec3;
import java.util.ArrayList;

public class RigidBodyGameObject extends GameObject
{
  public float mass = 1.0f;             //質量
  public Vec3 velocity = new Vec3( 0, 0, 0 );          //速度
  public ArrayList<Vec3> forces = new ArrayList< Vec3 >(); //力
  public ArrayList<Float> collision_radiuses = new ArrayList< Float >( );

  @Override
  public void update( final long delta_time_in_ns )
  {
    Vec3 acceleration = new Vec3( 0.0f, 0.0f, 0.0f );
    Vec3 force_sum    = new Vec3( 0.0f, 0.0f, 0.0f );

    for( Vec3 f: forces )
      force_sum = force_sum.add( f );

    acceleration = force_sum.multiply( 1.0f / mass );
    velocity = velocity.add( acceleration.multiply( delta_time_in_ns ) );
    position = velocity.add( velocity.multiply( delta_time_in_ns ) );

    super.update( delta_time_in_ns );
  }
}