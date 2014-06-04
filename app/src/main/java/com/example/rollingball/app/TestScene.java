package com.example.rollingball.app;
import java.nio.FloatBuffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;
import com.hackoeur.jglm.Vec3;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView.BufferType;
import android.graphics.Color;

public class TestScene extends Scene implements Renderer
{
  TestScene( final SceneManager s )
  {
    super( s );

    // ログを出力
    Log.v( "tag", "message" );

   
  }

  // InputManagerから値を読み取る
  public Vec3 test = input_manager.rotation;
  float[] ver_i  = test.getArray();

  @Override
  public void update( final long delta_time_in_ns )
  {
    super.update( delta_time_in_ns );
    GLES20.glClearColor( 0.9f, 0.9f, 1.0f, 1.0f ); // 初期化
  }

  @Override
  public void onSurfaceCreated(final GL10 gl10, final EGLConfig eglConfig)
  {

  }

  @Override
  // 変更されたときに呼び出し
  public void onSurfaceChanged( final GL10 gl, final int width, final int height )
  {
    //描画範囲の指定
    gl.glViewport(0, 0, width, height);
  }

  @Override
  public void onDrawFrame( final GL10 gl)
  {
    // xyz --> rgb;色を割り当てて、色の変化で傾きをとれていることを確認できるようにする。
    int vertexIdx = 0;

    float[] vertices = new float[3*6];

    // 原点
    vertices[ vertexIdx++ ] = ver_i[ 0 ];
    vertices[ vertexIdx++ ] = ver_i[ 1 ];
    vertices[ vertexIdx++ ] = ver_i[ 2 ];
    vertices[ vertexIdx++ ] = 0.0f;       // x
    vertices[ vertexIdx++ ] = 0.0f;       // y
    vertices[ vertexIdx++ ] = 1.0f;       // z

    // 右上
    vertices[ vertexIdx++ ] = ver_i[ 3 ];
    vertices[ vertexIdx++ ] = ver_i[ 4 ];
    vertices[ vertexIdx++ ] = ver_i[ 5 ];
    vertices[ vertexIdx++ ] = 1.0f;
    vertices[ vertexIdx++ ] = 1.0f;
    vertices[ vertexIdx++ ] = 1.0f;

    // 右下
    vertices[ vertexIdx++ ] = ver_i[ 6 ];
    vertices[ vertexIdx++ ] = ver_i[ 7 ];
    vertices[ vertexIdx++ ] = ver_i[ 8 ];
    vertices[ vertexIdx++ ] = 1.0f;
    vertices[ vertexIdx++ ] = 0.0f;
    vertices[ vertexIdx++ ] = 1.0f;

    ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
    bb.order(ByteOrder.nativeOrder());
    // ログを出力
    Log.v( "tag", "message" );

    FloatBuffer fb = bb.asFloatBuffer();
    fb.put(vertices);
    //ログ出力
    Log.v( "tag", "message" );

    //頂点3点を使って三角形を描画
    gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 3);
  }
}