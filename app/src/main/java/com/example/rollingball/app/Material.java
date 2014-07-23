package com.example.rollingball.app;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import com.hackoeur.jglm.Vec3;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Material implements IDrawable
{
  Vec3  _diffuse_color = new Vec3( 1.0f, 0.8f, 0.8f );
  float _transparent   = 0.0f;

  float _diffuse_texture_blending_factor = 0.0f;
  int   _diffuse_texture_id = 0;

  FloatBuffer diffuse_color_buffer = _diffuse_color.getBuffer();

  public Material diffuse_color( final Vec3 diffuse_color )
  {
    _diffuse_color = diffuse_color;
    diffuse_color_buffer = _diffuse_color.getBuffer();

    return this;
  }

  public Material transparent( final float transparent )
  {
    _transparent = transparent;
    return this;
  }

  public Material diffuse_texture_blending_factor( final float f )
  {
    if ( _diffuse_texture_id == 0 )
      Log.w( "Material.diffuse_texture_blending_factor()", "_diffuse_texture_id is 0." );

    _diffuse_texture_blending_factor = f;
    return this;
  }

  // set
  public Material diffuse_texture( final Bitmap image )
  {
    if ( _diffuse_texture_id > 0 )
      diffuse_texture();

    int[] ts = new int[]{ 0 };
    GLES20.glGenTextures( 1, ts, 0 );

    GLES20.glBindTexture( GLES20.GL_TEXTURE_2D, _diffuse_texture_id );

    GLUtils.texImage2D( GL10.GL_TEXTURE_2D, 0, image, 0 );

    GLES20.glTexParameteri( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR );
    GLES20.glTexParameteri( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR );
    GLES20.glTexParameteri( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT );
    GLES20.glTexParameteri( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT );

    image.recycle();

    _diffuse_texture_blending_factor = 1.0f;

    return this;
  }

  // remove
  public Material diffuse_texture()
  {
    GLES20.glDeleteTextures( 1, new int[]{ _diffuse_texture_id }, 0 );
    _diffuse_texture_id = 0;
    _diffuse_texture_blending_factor = 0.0f;
    return this;
  }

  public void draw( int program_id )
  {
    // 拡散反射光の色
    GLES20.glUniform3fv( GLES20.glGetUniformLocation( program_id, "diffuse" ), 1, diffuse_color_buffer );

    // 透明度
    GLES20.glUniform1f( GLES20.glGetUniformLocation( program_id, "transparent" ), _transparent );

    if ( _diffuse_texture_id > 0 )
    {
      // テクスチャーのブレンド比
      GLES20.glUniform1f( GLES20.glGetUniformLocation( program_id, "diffuse_texture_blending_factor" ), _diffuse_texture_blending_factor );

      // テクスチャーユニットの有効化
      GLES20.glActiveTexture( 0 );

      // テクスチャーの有効化
      GLES20.glBindTexture( GLES20.GL_TEXTURE_2D, _diffuse_texture_id );
    }
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