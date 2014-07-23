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

  static public Vec3 multiply_high_precision( final Vec3 a, final Vec3 b)
  {
    return new Vec3
      ( multiply_high_precision( a.getX(), b.getX() )
      , multiply_high_precision( a.getY(), b.getY() )
      , multiply_high_precision( a.getZ(), b.getZ() )
      );
  }

  static public Vec3 multiply_high_precision( final Vec3 a, final Vec3 b, final float c)
  {
    return new Vec3
      ( multiply_high_precision( a.getX(), b.getX(), c )
      , multiply_high_precision( a.getY(), b.getY(), c )
      , multiply_high_precision( a.getZ(), b.getZ(), c )
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

  static public Vec3 multiply_high_precision( final Vec3 a, final float b, final float c )
  { return multiply_high_precision( a, (float)( (double)b * (double)c ) ); }

  static public float multiply_high_precision( final float a, final float b, final float c )
  { return (float)( (double)a * (double)b * (double)c ); }

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

  static public Vec3 xz( final Vec3 a, final float x, final float z )
  { return new Vec3( x, a.getY(), z ); }

  // 物体Aと物体Bの衝突に伴う反発（完全弾性衝突と仮定）後の速度を計算
  //   運動量保存則に基づき、X,Y,Z軸それぞれにベクターを分離して考える。
  //   厳密ではないが凡そ今回のゲームの為にはうまく行く。
  //   参考: http://www.wakariyasui.sakura.ne.jp/b2/52/5232nibuttai.html
  static public Vec3[] collided_velocities( final float ma, final float mb, final Vec3 va, final Vec3 vb )
  {
    // double 分解能化
    final double ma_ = (double)ma;
    final double mb_ = (double)mb;
    final double va_x_ = (double)va.getX();
    final double va_y_ = (double)va.getY();
    final double va_z_ = (double)va.getZ();
    final double vb_x_ = (double)vb.getX();
    final double vb_y_ = (double)vb.getY();
    final double vb_z_ = (double)vb.getZ();

    // 定数項の整理
    final double tmp_a_1 = ( ma_ - mb_ ) * va_x_;
    final double tmp_a_2 = 2.0 * mb_;
    final double tmp_a_3 = 1.0 / ( ma_ + mb_ );

    // va1 = f(va0) = ( a1 + a2 * va0 ) / a3
    final double collided_va_x = ( tmp_a_1 + tmp_a_2 * vb_x_ ) * tmp_a_3;
    final double collided_va_y = ( tmp_a_1 + tmp_a_2 * vb_y_ ) * tmp_a_3;
    final double collided_va_z = ( tmp_a_1 + tmp_a_2 * vb_z_ ) * tmp_a_3;

    // 運動量保存則より
    // vb = va0 - vb0 + va1
    final double collided_vb_x = va_x_ - vb_x_ + collided_va_x;
    final double collided_vb_y = va_y_ - vb_y_ + collided_va_y;
    final double collided_vb_z = va_z_ - vb_z_ + collided_va_z;

    Vec3[] r =
      { new Vec3( (float)collided_va_x, (float)collided_va_y, (float)collided_va_z )
      , new Vec3( (float)collided_vb_x, (float)collided_vb_y, (float)collided_vb_z )
      };

    return r;
  }

  // 物体A、物体Bが距離distanceで衝突した際に、擬似的に物体間のめり込みを解消
  static public Vec3[] collided_positions( final Vec3 pa, final Vec3 pb, final float distance )
  {
    // 衝突の軸
    final Vec3 axis = subtract_high_precision( pb, pa ).getUnitVector();
    final Vec3 move = multiply_high_precision( axis, distance, 1.0f + positive_bias );

    Vec3[] r =
      { add_high_precision( pa, move )
      , subtract_high_precision( pb, move )
      };
    return r;
  }

  static public Vec3 vec3( final float v )
  { return new Vec3( v, v, v ); }

  static public float xy_length( final Vec3 v )
  { return (float)Math.sqrt( v.getX() * v.getX() + v.getY() * v.getY() ); }
}