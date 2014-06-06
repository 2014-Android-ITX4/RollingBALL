package com.example.rollingball.app;
import android.app.Activity;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;
import com.hackoeur.jglm.Vec3;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class TestScene extends Scene implements Renderer
{

  //値の計算
  //protected final static double rad = 180 / Math.PI;

  // InputManagerから値を読み取る
  /*float[] ver_i = new float[ 3 ];
  int i_x = new Integer(0);
  int i_y = new Integer(0);
  int i_z = new Integer(0);*/

  TestScene( final SceneManager s )
  {
    super( s );

    // ログを出力
    //Log.d( "RollingBALL", "message" );
  }

  @Override
  public void update( final long delta_time_in_ns )
  {
    super.update( delta_time_in_ns );
    // xyz --> rgb;色を割り当てて、色の変化で傾きをとれていることを確認できるようにする。
    GLES20.glClearColor(
      input_manager.rotation.getX(),
      input_manager.rotation.getY(),
      input_manager.rotation.getZ(),
      1.0f
    );
    /*
    GLES20.glClearColor( 0.9f, 0.9f, 1.0f, 1.0f ); // 初期化
    int i_x = (int)(input_manager.rotation.getX() * rad);
    int i_y = (int)(input_manager.rotation.getY() * rad);
    int i_z = (int)(input_manager.rotation.getZ() * rad);

    GLES20.glClearColor(i_x,i_y,i_z,1.0f);*/
  }

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
    /*
    int i_x = (int)(input_manager.rotation.getX() * rad);
    int i_y = (int)(input_manager.rotation.getY() * rad);
    int i_z = (int)(input_manager.rotation.getZ() * rad);

    GLES20.glClearColor(i_x,i_y,i_z,1.0f);
    */
  }
}