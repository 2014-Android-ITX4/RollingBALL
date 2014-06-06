package com.example.rollingball.app;
import android.opengl.GLES20;

public class TestScene extends Scene
{

  TestScene( final SceneManager s )
  {
    super( s );
  }

  @Override
  public void update( final long delta_time_in_ns )
  {
    super.update( delta_time_in_ns );
    GLES20.glClearColor( 0.9f, 0.9f, 1.0f, 1.0f ); // 初期化

    // InputManagerから値を読み取る
    float x = input_manager.rotation.getX();
    float y = input_manager.rotation.getY();
    float z = input_manager.rotation.getZ();

    //[-π-π]の範囲を[0-1.0]に変換する
    while ( x < 0.0f ) x += 1.0f;
    while ( y < 0.0f ) y += 1.0f;
    while ( z < 0.0f ) z += 1.0f;
    x = x * 1/2;
    y = y * 1/2;
    z = z * 1/2;

    // xyz --> rgb;色を割り当てて、色の変化で傾きをとれていることを確認できるようにする。
    GLES20.glClearColor(x,y,z,1.0f);
  }

  /*
  @Override
  public void onSurfaceCreated( final GL10 gl10, final EGLConfig eglConfig )
  {

  }

  @Override
  public void onSurfaceChanged( final GL10 gl, final int width, final int height )
  {

  }

  @Override
  public void onDrawFrame( final GL10 gl )
  {

  }
  */
}