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

    final float pif = (float)Math.PI;
    final float inversed_pih = (float)( 0.5 / Math.PI );

    // for debug LOG
    Log.d( "RollingBALL", input_manager.rotation.toString() );

    // InputManagerから値[-π..+π]を読み取って[0.0..+1.0]にする。
    float r = ( input_manager.rotation.getX() + pif ) * inversed_pih;
    float b = ( input_manager.rotation.getZ() + pif ) * inversed_pih;

    // xyz --> rgb;色を割り当てて、色の変化で傾きをとれていることを確認できるようにする。
    GLES20.glClearColor( r, 0.5f, b, 1.0f );
  }
}