package com.example.rollingball.app;

import com.hackoeur.jglm.Vec3;

// 各種ヘルパー
//   *_high_precision: 計算精度の都合 float のままで計算すると不幸な事故が起こる場合向け
public class Helper
{
  static public final float positive_bias = +1.0e-3f;
  static public final float negative_bias = -positive_bias;

  static public final float wall_friction_factor = 0.6f;

  static public Vec3 add_high_precision( final Vec3 a, final Vec3 b)
  {
    return new Vec3
      ( add_high_precision( a.getX(), b.getX() )
      , add_high_precision( a.getY(), b.getY() )
      , add_high_precision( a.getZ(), b.getZ() )
      );
  }

  static public Vec3 subtract_high_precision( final Vec3 a, final Vec3 b)
  {
    return new Vec3
      ( subtract_high_precision( a.getX(), b.getX() )
      , subtract_high_precision( a.getY(), b.getY() )
      , subtract_high_precision( a.getZ(), b.getZ() )
      );
  }

  static public Vec3 multiply_high_precision( final Vec3 a, final float b)
  {
    return new Vec3
      ( multiply_high_precision( a.getX(), b )
      , multiply_high_precision( a.getY(), b )
      , multiply_high_precision( a.getZ(), b )
      );
  }

  static public Vec3 multiply_x_high_precision( final Vec3 a, final float b )
  { return x( a, multiply_high_precision( a.getX(), b ) ); }

  static public Vec3 multiply_y_high_precision( final Vec3 a, final float b )
  { return y( a, multiply_high_precision( a.getY(), b ) ); }

  static public Vec3 multiply_z_high_precision( final Vec3 a, final float b )
  { return z( a, multiply_high_precision( a.getZ(), b ) ); }

  static public float add_high_precision( final float a, final float b )
  { return (float)( (double)a + (double)b ); }

  static public float subtract_high_precision( final float a, final float b )
  { return (float)( (double)a - (double)b ); }

  static public float multiply_high_precision( final float a, final float b )
  { return (float)( (double)a * (double)b ); }

  static public float divide_high_precision( final float a, final float b )
  { return (float)( (double)a / (double)b ); }

  static public Vec3 x( final Vec3 a, final float b )
  { return new Vec3( b, a.getY(), a.getZ() ); }

  static public Vec3 y( final Vec3 a, final float b )
  { return new Vec3( a.getX(), b, a.getZ() ); }

  static public Vec3 z( final Vec3 a, final float b )
  { return new Vec3( a.getX(), a.getY(), b ); }

  static public Vec3 negate_x( Vec3 a )
  { return new Vec3( -a.getX(), a.getY(), a.getZ() ); }

  static public Vec3 negate_y( Vec3 a )
  { return new Vec3( a.getX(), -a.getY(), a.getZ() ); }

  static public Vec3 negate_z( Vec3 a )
  { return new Vec3( a.getX(), a.getY(), -a.getZ() ); }
}
