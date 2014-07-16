package com.example.rollingball.app;

import android.util.Log;

import com.hackoeur.jglm.Vec3;

public class InputManager implements IUpdatable
{

  public Vec3 rotation = new Vec3( 0.0f, 0.0f, 0.0f );
  private Scene _scene;
  public float scale, old_scale, result_scale;

  InputManager(Scene arg_scene)
  {
    _scene = arg_scene;
    scale = 1.0f;
    old_scale = 1.0f;
    result_scale = 0.0f;

  }

  @Override
  public void update( final float delta_time_in_seconds )
  {
    rotation = _scene.scene_manager.view.activity.rotation;

    old_scale = scale;
    scale = _scene.scene_manager.view.activity.scale;

    if ( scale != 1.0f )
    {
      result_scale = ( scale - old_scale ) * 5;
//      Log.d("InputManager", String.valueOf( result_scale ) );
    }else{
      result_scale = 0.0f;
    }

  }

}
