package com.example.rollingball.app;

import com.hackoeur.jglm.Vec3;

public class InputManager implements IUpdatable
{

  public Vec3 rotation = new Vec3( 0.0f, 0.0f, 0.0f );
  private Scene _scene;

  InputManager(Scene arg_scene)
  {
    _scene = arg_scene;
  }

  @Override
  public void update( final long delta_time_in_ns )
  {
    rotation = _scene.scene_manager.view.activity.orientation;
  }

}
