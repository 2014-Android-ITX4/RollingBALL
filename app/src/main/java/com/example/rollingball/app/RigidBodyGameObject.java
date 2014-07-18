package com.example.rollingball.app;

import android.util.Log;

import com.hackoeur.jglm.Vec3;
import java.util.ArrayList;

public class RigidBodyGameObject extends GameObject
{
  public float mass = 1.0f;             //質量
  public Vec3 velocity = new Vec3( 0, 0, 0 );          //速度
  public ArrayList<Vec3> forces = new ArrayList< Vec3 >( ); //力
  public ArrayList< BoundingSphere > collision_boundings = new ArrayList< BoundingSphere>( );

  private Vec3 _force_sum;

  public RigidBodyGameObject( Scene scene )
  { super( scene );}

  @Override
  public void update( final float delta_time_in_seconds )
  {
    _force_sum = new Vec3( 0.0f, 0.0f, 0.0f );

    for( int i = 0; i < forces.size(); i++ )
      _force_sum = _force_sum.add( forces.get( i ) );

    forces.clear();

    Vec3 acceleration = _force_sum.multiply( 1.0f / mass );
    velocity = velocity.add( acceleration.multiply( delta_time_in_seconds ) );
    position = position.add( velocity.multiply( delta_time_in_seconds ) );

    if ( StageScene.class.isInstance( _scene ) )
    {
      StageScene s = (StageScene)_scene;
      if ( position.getY() < s.death_height() )
        position = new Vec3( position.getX(), s.death_height(), position.getZ() );
    }

    // #233 擬似的な摩擦の実装
//    Log.d( "", ""+velocity.toString() );
    final float pseudo_friction_factor_horizon  = 0.998f;
    final float pseudo_friction_factor_vertical = 1.0f;
    velocity = new Vec3
      ( velocity.getX() * pseudo_friction_factor_horizon
      , velocity.getY() * pseudo_friction_factor_vertical
      , velocity.getZ() * pseudo_friction_factor_horizon
      );

    super.update( delta_time_in_seconds );
  }

  @Override
  public void effect_gravity( final Vec3 g_in_m_per_s_s )
  {
    this.forces.add( g_in_m_per_s_s );
  }
}