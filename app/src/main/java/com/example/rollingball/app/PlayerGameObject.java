package com.example.rollingball.app;

import com.hackoeur.jglm.Vec3;
import java.util.LinkedList;

<<<<<<< HEAD
public class PlayerGameObject extends LifeGameObject
=======
public class PlayerGameObject extends RigidBodyGameObject implements IUpdatable
>>>>>>> origin/#104SceneCamera作成
{
  public LinkedList< Item > items;
  public InputManager input_manager;

  PlayerGameObject( final InputManager s )
  {}

  @Override
  public void update( final long delta_time_in_ns )
  {
    // 傾きからかかる力を計算
    // 傾きベクターの値の範囲 [ -π .. +π ] [rad]
    // 力ベクターの値の範囲 [ -10 .. 10 ] [N] ( = [kg・m/(s・s)] )
    // ハムスターの標準体重: 1.0e-1 [kg]
    // 標準体重のハムスターを1[秒間]に1[m/(ss)]加速する程度の力f: 1.0e-1 [N]
    final float max_force = 1.0e-1f;

    // 傾きに応じて速度に力(f)を追加する
    forces.add( input_manager.rotation.multiply( max_force ) );

    super.update( delta_time_in_ns );
  }
}
