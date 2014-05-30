package com.example.rollingball.app;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;
import com.hackoeur.jglm.Vec3;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


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
  float[] vertices  = test.getArray();

  @Override
  public void update( final long delta_time_in_ns )
  {
    super.update( delta_time_in_ns );
    GLES20.glClearColor( 0.9f, 0.9f, 1.0f, 1.0f ); // 初期化
  }

  @Override
  public void onSurfaceCreated(
    final GL10 gl10, final EGLConfig eglConfig
  )
  {

  }

  @Override
  // 大きさが変更されたときに呼び出し
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

    // 原点
    vertices[vertexIdx++] = 0.0f;       // x
    vertices[vertexIdx++] = 0.0f;       // y
    vertices[vertexIdx++] = 1.0f;       // z

    // 右上
    vertices[vertexIdx++] = 1.0f;
    vertices[vertexIdx++] = 1.0f;
    vertices[vertexIdx++] = 1.0f;

    // 右下
    vertices[vertexIdx++] = 1.0f;
    vertices[vertexIdx++] = 0.0f;
    vertices[vertexIdx++] = 1.0f;

    // 頂点座標を有効にする
    gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
    //頂点カラーを有効にする
    GLES20.glEnableVertexAttribArray(this.mLocColor);
    
    //頂点3点を使って三角形を描画
    gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 3);
  }
}