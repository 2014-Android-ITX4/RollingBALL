package com.example.rollingball.app;

import com.hackoeur.jglm.Vec;
import com.hackoeur.jglm.Vec3;

public class BoundingSphere
{
  final GameObject _master;
  final Vec3       _center_diff;
  final float      _radius;


  public BoundingSphere( final GameObject master, final float radius, final Vec3 center_diff )
  {
    _master      = master;
    _center_diff = center_diff;
    _radius      = radius;
  }

  public BoundingSphere( final GameObject master, final float radius )
  { this( master, radius, Vec3.VEC3_ZERO ); }

  // return: true ならば接触またはめりこんでいる
  public boolean intersect_bool( final BoundingSphere target )
  { return intersect( target ) <= 0.0f; }

  // return: 距離、0.0f以下なら接触またはめり込んでいる
  public float intersect( final BoundingSphere target )
  {
    final float distance = target.position().subtract( position() ).getLength();
    final float border   = target.radius() + _radius;
    return  distance - border;
  }

  // return: true ならば接触またはめりこんでいる
  public boolean intersect_filed_bool( final Vec3 field_position )
  { return intersect_field_bool( field_position, new Vec3( 0.0f, 1.0f, 0.0f ) ); }

  // return: 距離、0.0f以下なら接触またはめり込んでいる
  public float intersect_field( final Vec3 field_position )
  { return intersect_field( field_position, new Vec3( 0.0f, 1.0f, 0.0f ) ); }

  // return: true ならば接触またはめりこんでいる
  public boolean intersect_field_bool( final Vec3 field_position, final Vec3 field_normal )
  {
    return intersect_field( field_position, field_normal ) < _radius;
  }

  // return: 距離、0.0f以下なら接触またはめり込んでいる
  public float intersect_field( final Vec3 field_position, final Vec3 field_normal )
  {
    final float distance = field_normal.dot( position().subtract( field_position ) ) / field_normal.getLength();
    return distance;
  }

  public GameObject master() { return  _master; }
  public Vec3 center_diff() { return _center_diff; }
  public float radius() { return _radius; }

  public Vec3 position() { return _master.position.add( _center_diff ); }
}
