package com.example.rollingball.app;

import android.opengl.GLES20;

import com.hackoeur.jglm.Vec3;

import java.nio.IntBuffer;

public class Material implements IDrawable
{
  Vec3  _diffuse_color = new Vec3( 1.0f, 0.9f, 0.9f );
  float _transparent   = 0.0f;

  public Material diffuse_color( final Vec3 diffuse_color )
  {
    _diffuse_color = diffuse_color;
    return this;
  }

  public Material transparent( final float transparent )
  {
    _transparent = transparent;
    return this;
  }

  public void draw( int program_id )
  {

    // 拡散反射光の色
    GLES20.glUniform3fv( GLES20.glGetUniformLocation( program_id, "diffuse" ), 1, _diffuse_color.getBuffer( ) );

    // 不透明度
    GLES20.glUniform1f( GLES20.glGetUniformLocation( program_id, "transparent" ), _transparent );

    // TODO: テクスチャー対応用。テクスチャーに対応する際にどうぞ
    // テクスチャーのブレンド比
    //GLES20.glUniform1f( GLES20.glGetUniformLocation( program_id, "location_of_diffuse_texture_blending_factor" ), 0.0f );

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

}
