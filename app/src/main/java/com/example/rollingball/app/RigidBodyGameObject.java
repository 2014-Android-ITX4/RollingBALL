package com.example.rollingball.app;

import com.hackoeur.jglm.Vec3;
import java.util.ArrayList;

/**
 * Created by sakamoto on 2014/05/14.
 */
public class RigidBodyGameObject extends GameObject
{
  public float mass;                //質量
  public Vec3 velocity;//速度
  public ArrayList<Vec3> forces;    //力

  @Override
  public void update( final long delta_time_in_ns )
  {
    super.update( delta_time_in_ns );
    Vec3 acceleration;
    Vec3 force_sum = null;

    for( Vec3 v: forces ){
      force_sum = force_sum.add( v );
    }

    acceleration = force_sum.multiply( 1 / mass );
    velocity = velocity.add( acceleration.multiply( delta_time_in_ns ) );
    super.position = velocity.add( velocity.multiply( delta_time_in_ns ) );

  }
}
