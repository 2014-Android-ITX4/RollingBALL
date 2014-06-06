package com.example.rollingball.app;
import android.opengl.GLES20;
import android.util.Log;

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

    float inversed_pi = (float)( 1.0 / Math.PI );

    // for debug LOG
    Log.d( "RollingBALL", input_manager.rotation.toString() );

    // InputManagerから値[-π..+π]を読み取って[-1.0..+1.0]にする。
    float x = input_manager.rotation.getX() * inversed_pi;
    float y = input_manager.rotation.getY() * inversed_pi;
    float z = input_manager.rotation.getZ() * inversed_pi;

    //[-1.0..+1.0]の範囲を[0.0..+1.0]に変換する。
    while ( x < 0.0f ) x += 1.0f;
    while ( y < 0.0f ) y += 1.0f;
    while ( z < 0.0f ) z += 1.0f;

    // xyz --> rgb;色を割り当てて、色の変化で傾きをとれていることを確認できるようにする。
    GLES20.glClearColor(x, y, z, 1.0f);
  }
}