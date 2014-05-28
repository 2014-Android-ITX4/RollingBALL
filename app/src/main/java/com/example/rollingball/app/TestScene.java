package com.example.rollingball.app;
import android.opengl.GLES20;
import android.util.Log;

import com.hackoeur.jglm.Vec;
import com.hackoeur.jglm.Vec3;

import javax.microedition.khronos.opengles.GL10;

public class TestScene extends Scene
{
  private static final float[] Position = {0.5f, 0.5f, 0.0f};
  float[] rotation_values = new float[3];

  TestScene( final SceneManager s )
  {
    super( s );

    // ログを出力
    Log.v( "tag", "message" );

    // xyz --> rgb;色を割り当てて、色の変化で傾きをとれていることを確認できるようにする。
    rotation_values = InputManager rotation;

    // 描画処理
    public void draw( GL10 gl ){

    // 頂点配列を有効化します
    gl.glEnableClientState( GL10.GL_VERTEX_ARRAY );
    gl.glVertexPointer( 3, GL10.GL_FLOAT , 0, rotation_values);

    gl.glNormal3f( 1.0f, 0.0f, 0.5f ); // x　赤

    gl.glNormal3f( 0.0f, 0.0f, 1.0f ); // y　青

    gl.glNormal3f( 1.0f, 1.0f, 0.0f ); // z　黄
    }
   }

  @Override
  public void update( final long delta_time_in_ns )
  {
    super.update( delta_time_in_ns );
    GLES20.glClearColor( 0.9f, 0.9f, 1.0f, 1.0f ); // 初期化
  }
}