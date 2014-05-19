package com.example.rollingball.app;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.view.KeyEvent;
import com.hackoeur.jglm.Vec3;
import android.hardware.SensorManager;
import android.os.Bundle;

/**
 * Created by Tukasa on 2014/05/14.
 */
public class InputManager implements IUpdatetable
{

  private boolean _keyboard_mode = false;
  public Vec3 rotation = new Vec3();
  private Scene _scene;

  InputManager(Scene arg_scene)
  {
    _scene = arg_scene;
  }

  @Override
  public void update( final long delta_time_in_ns )
  {
    update_from_keyboard();
    update_from_gyroscope();
  }

  private void update_from_keyboard()
  {
    // ジャイロ入力状態なら何もせず抜ける
    if( _keyboard_mode == false)
      return;
  }

  private void update_from_gyroscope()
  {
    // キーボード入力状態なら何もせず抜ける
    if( _keyboard_mode == true )
      return;
  }


}
