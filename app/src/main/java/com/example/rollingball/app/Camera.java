package com.example.rollingball.app;


import com.hackoeur.jglm.Vec3;

/**
 * Created by Tukasa on 2014/05/14.
 */
public class Camera implements IUpdatable
{
  public Vec3 eye;
  public Vec3 look_at;
  public Vec3 up;
  public float field_of_view;
  public Scene scene;

  public void Camera( final Scene scene_ )
  {
    scene = scene_;
  }

  @Override
  public void update( final long delta_time_in_ns )
  {

  }
}
