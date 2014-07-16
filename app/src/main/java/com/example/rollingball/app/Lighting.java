package com.example.rollingball.app;

import android.opengl.GLES20;

import com.hackoeur.jglm.Vec3;

import java.nio.IntBuffer;

public class Lighting implements IDrawable
{
  Vec3 _position = new Vec3( 0.0f, 1.0f, 0.0f );
  Vec3 _target = Vec3.VEC3_ZERO;

  // updateで更新
  Vec3 _direction = new Vec3( 0.0f, -1.0f, 0.0f );

  // 距離に線形比例して光を弱めたい場合に0.0以上の値を設定する
  float _linear_attenuation = 0.05f;

  // 環境光
  Vec3 _ambient_color = new Vec3( 0.05f, 0.05f, 0.05f );

  // 光の色
  Vec3 _color = new Vec3( 1.0f, 1.0f, 1.0f );

  public Lighting color( final Vec3 c )
  {
    _color = c;
    return this;
  }

  public Lighting ambient_color( final Vec3 c )
  {
    _ambient_color = c;
    return this;
  }

  public Lighting position( final Vec3 p )
  {
    _position = p;
    update();
    return this;
  }

  public Lighting target( final Vec3 t )
  {
    _target = t;
    update();
    return this;
  }

  public Lighting linear_attenuation( final float a )
  {
    _linear_attenuation = a;
    return this;
  }

  public Lighting update()
  {
    _direction =  _target.subtract( _position );
    return this;
  }

  @Override
  public void draw()
  {
    // シェーダープログラムの取得
    IntBuffer program_id_buffer = IntBuffer.allocate( 1 );
    GLES20.glGetIntegerv( GLES20.GL_CURRENT_PROGRAM, program_id_buffer );
    int program_id = program_id_buffer.get();

    draw( program_id );
  }

  public void draw( int program_id )
  {
    // 並行光源の向き
    GLES20.glUniform3fv(
      GLES20.glGetUniformLocation( program_id, "light_direction" ),
      1,
      _direction.getBuffer()
    );

    // 環境光の色
    GLES20.glUniform3fv(
      GLES20.glGetUniformLocation( program_id, "ambient" ),
      1,
      _ambient_color.getBuffer()
    );
  }
}