package com.example.rollingball.app;

import android.util.Log;

import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Vec3;

public class GameObject implements IUpdatable, IDrawable
{
  public Vec3      position = new Vec3( 0.0f, 0.0f, 0.0f );
  public ModelData model    = ModelData.generate_cube( 1.0f );
  protected Scene  _scene   = null;

  public GameObject( Scene scene )
  { _scene = scene; }

  @Override
  public void update( final float delta_time_in_seconds )
  {

  }

  @Override
  public void draw()
  {
    //Log.d( "position", position.toString() );
    model.draw( Mat4.MAT4_IDENTITY.translate( position ) );
  }

  public void on_resume()
  {
    model.on_resume();
  }

  public void effect_gravity( final Vec3 g_in_m_per_s_s )
  {

  }

}