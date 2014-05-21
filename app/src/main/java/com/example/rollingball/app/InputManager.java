package com.example.rollingball.app;

import com.hackoeur.jglm.Vec3;

/**
 * Created by Tukasa on 2014/05/14.
 */
public class InputManager implements IUpdatetable
{

  private boolean _keyboard_mode = false;
  public Vec3 rotation = new Vec3();
  private Scene _scene;
  private float orientation;

  InputManager(Scene arg_scene)
  {
    _scene = arg_scene;
  }

  @Override
  public void update( final long delta_time_in_ns )
  {
    update_from_keyboard();
    update_from_orientation();
  }

  private void update_from_keyboard()
  {
    // 傾き入力状態なら何もせず抜ける
    if( _keyboard_mode == false)
      return;
    // 未実装
    throw new NotImplementedException();
  }

  private void update_from_orientation()
  {
    // キーボード入力状態なら何もせず抜ける
    if( _keyboard_mode == true )
      return;

    // 未実装
    orientation = _scene.scene_manager.view.activity.orientation_values;
  }


}
