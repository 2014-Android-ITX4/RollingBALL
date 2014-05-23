package com.example.rollingball.app;

import com.hackoeur.jglm.Vec3;

public class GameObject implements IUpdatable, IDrawable
{
  public Vec3 position;
  public ModelData model;
  protected Scene _scene;

  @Override
  public void update( final long delta_time_in_ns )
  {

  }

  @Override
  public void draw()
  {

  }

}