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
    //final float max_force = 1.0e-1f;
    final float max_force = 1.0e-1f *8;
    /*if ( Math.sin( rotation_theta )>0 &&Math.cos( rotation_theta )>0 || Math.sin( rotation_theta )<0 &&Math.cos( rotation_theta )<0)
    input_manager.rotation = new Vec3
      ( input_manager.rotation.getX() * (float)Math.cos(rotation_theta ) + input_manager.rotation.getZ() * (float)Math.sin( rotation_theta )
        , 0.0f
        , input_manager.rotation.getX() * (float)Math.sin( rotation_theta ) - input_manager.rotation.getZ() * (float)Math.cos( rotation_theta )
      );
    else {
      input_manager.rotation = new Vec3
        ( input_manager.rotation.getX() * (float)Math.cos(rotation_theta ) - input_manager.rotation.getZ() * (float)Math.sin( rotation_theta )
          , 0.0f
          , input_manager.rotation.getX() * (float)Math.sin( rotation_theta ) + input_manager.rotation.getZ() * (float)Math.cos( rotation_theta )
        );
    }*/
    float x = input_manager.rotation.getX();
    float z = input_manager.rotation.getZ();

    float rotation_theta = (float)Math.acos( z );

    // 傾きに応じてオブジェクトの力群forcesに力fを追加する
    forces.add( input_manager.rotation.multiply( max_force ) );


    super.update( delta_time_in_seconds );
  }

}
