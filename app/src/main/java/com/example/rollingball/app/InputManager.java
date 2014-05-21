package com.example.rollingball.app;

import com.hackoeur.jglm.Vec3;

/**
 * Created by Tukasa on 2014/05/14.
 */
public class InputManager implements IUpdatetable
{

  public Vec3 rotation;
  private Scene _scene;
  private float orientation[];

  InputManager(Scene arg_scene)
  {
    _scene = arg_scene;
  }

  @Override
  public void update( final long delta_time_in_ns )
  {
    orientation = _scene.scene_manager.view.activity.orientation_values;
    rotation = new Vec3( orientation[ 0 ], orientation[ 1 ], orientation[ 2 ] );
  }

}
