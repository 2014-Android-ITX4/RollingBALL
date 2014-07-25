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
  private Vec3 _force_sum_reset = new Vec3( 0.0f, 0.0f, 0.0f );

  private Vec3 _acceleration;

  private StageScene _s;

  final float pseudo_friction_factor_horizon  = 0.998f;
  final float pseudo_friction_factor_vertical = 1.0f;

  public RigidBodyGameObject( Scene scene )
  { super( scene );}

  @Override
  public void update( final float delta_time_in_seconds )
  {
    _force_sum = _force_sum_reset;

    for( int i = 0; i < forces.size(); i++ )
      _force_sum = _force_sum.add( forces.get( i ) );

    forces.clear();

    _acceleration = Helper.multiply_high_precision( _force_sum, 1.0f / mass );
    velocity = Helper.add_high_precision( velocity, Helper.multiply_high_precision( _acceleration, delta_time_in_seconds ) );
    position = Helper.add_high_precision( position, Helper.multiply_high_precision( velocity, delta_time_in_seconds ) );

    if ( StageScene.class.isInstance( _scene ) )
    {
       _s = (StageScene)_scene;
      if ( position.getY() < _s.death_height() )
        position = new Vec3( position.getX(), _s.death_height(), position.getZ() );
    }

    // #233 擬似的な摩擦の実装
    //Log.d( "", ""+velocity.toString() );
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