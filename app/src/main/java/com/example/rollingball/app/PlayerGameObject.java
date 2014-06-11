package com.example.rollingball.app;

import com.hackoeur.jglm.Vec3;
import java.util.LinkedList;

public class PlayerGameObject extends RigidBodyGameObject implements IUpdatable
{
  PlayerGameObject( final InputManager s )
  {}
  public LinkedList< Item > items;
  public InputManager input_manager;

  @Override
  public void update( final long delta_time_in_ns )
  {
    // 傾きに応じて速度に力(F)を追加する
    forces.add(input_manager.rotation);

    Vec3 acceleration = new Vec3( 0.0f, 0.0f, 0.0f );
    Vec3 force_sum    = new Vec3( 0.0f, 0.0f, 0.0f );

    for( Vec3 f: forces )
      force_sum = force_sum.add( f );

    acceleration = force_sum.multiply( 1.0f / mass );
    velocity = velocity.add( acceleration.multiply( delta_time_in_ns ) );
  }
}
