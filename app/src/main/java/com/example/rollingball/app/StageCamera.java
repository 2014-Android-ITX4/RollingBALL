package com.example.rollingball.app;

import android.opengl.GLU;

import com.hackoeur.jglm.Vec3;

/**
 * Created by arakawa on 2014/05/21.
 */
public class StageCamera extends Camera
{
  public float distance;    //r
  public float theta = 0;   //θ
  public float phi = 30;    //φ

  @Override
  public void update( long delta_time_in_ns )
  {
    super.update( delta_time_in_ns );

    float x = distance * ( float ) Math.sin( theta ) * ( float ) Math.cos( phi );
    float y = distance * ( float ) Math.sin( theta ) * ( float ) Math.sin( phi );
    float z = distance * ( float ) Math.cos( theta );

    Vec3 a = new Vec3(x,y,z);




    this.eye = this.scene.game_objects

  }
}
