package com.example.rollingball.app;

import android.util.Log;

import com.hackoeur.jglm.Vec3;
import java.util.LinkedList;

public class PlayerGameObject extends LifeGameObject implements IUpdatable

{
  public LinkedList< Item > items;
  public InputManager input_manager;

  PlayerGameObject( Scene scene, final InputManager s )
  {
    super( scene );

    this.mass = 0.1f;
    this.collision_boundings.add( new BoundingSphere( this, 0.5f ) );
    input_manager = s;
  }

  @Override
  public void update( final float delta_time_in_seconds )
  {
    // 傾きからかかる力を計算
    // 傾きベクターの値の範囲 [ -π .. +π ] [rad]
    // 力ベクターの値の範囲 [ -10 .. 10 ] [N] ( = [kg・m/(s・s)] )
    // ハムスターの標準体重: 1.0e-1 [kg]
    // 標準体重のハムスターを1[秒間]に1[m/(ss)]加速する程度の力f: 1.0e-1 [N]

    final float max_force = 1.0e+1f;

    float x = input_manager.rotation.getX();
    float z = input_manager.rotation.getZ();
    float rotation_distance = ( float )Math.sqrt( ( float )Math.pow( x, 2 ) + ( float )Math.pow( z, 2 ) );
    float rotation_theta = ( float )Math.atan2( x, z );

    if ( rotation_theta != _scene.camera.theta_camera())
      rotation_theta += _scene.camera.theta_camera();
    x = rotation_distance * ( float )Math.sin( rotation_theta );
    z = rotation_distance * ( float )Math.cos( rotation_theta );
    input_manager.rotation = new Vec3( -x, 0.0f, -z );

    // 傾きに応じてオブジェクトの力群forcesに力fを追加する
    forces.add( input_manager.rotation.multiply( max_force ) );


    super.update( delta_time_in_seconds );
  }

}
