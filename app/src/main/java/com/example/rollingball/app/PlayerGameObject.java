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

    final double max_force = 1.0e+1f;

    // 傾き直交座標を入手
    double force_x = input_manager.rotation.getX();
    double force_z = input_manager.rotation.getZ();

    // 傾き直交座標を極座標へ
    double force_distance = Math.sqrt( force_x * force_x + force_z * force_z );
    double force_theta    = Math.atan2( force_x, force_z );

    // 傾き極座標の回転量にカメラによる見た目上の角度を加算
    force_theta += ((StageCamera)_scene.camera).theta();

    // 傾き極座標を傾き直交座標へ
    force_x = force_distance * Math.sin( force_theta );
    force_z = force_distance * Math.cos( force_theta );
    Vec3 force = new Vec3( (float)( force_x * max_force), 0.0f, (float)( force_z * max_force ) );

    // 傾きに応じてオブジェクトの力群forcesに力fを追加する
    forces.add( force );

    super.update( delta_time_in_seconds );
  }

}
