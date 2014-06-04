package com.example.rollingball.app;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;
import com.hackoeur.jglm.Vec3;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class TestScene extends Scene implements Renderer
{
  protected final static double rad = 180 / Math.PI;

  // InputManagerから値を読み取る
  float[] ver_i = new float[ 3 ];

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
    GLES20.glClearColor( 0.9f, 0.9f, 1.0f, 1.0f ); // 初期化
    GLES20.glClearColor(
      input_manager.rotation.getX(),
      input_manager.rotation.getY(),
      input_manager.rotation.getZ(),
      1.0f
    );
    // nullの場合のログを出力
    //Log.d( "RollingBALL", "message" );
    //Log.d("RollingBALL", isNullOrZeroLength("") + "");
  }

  //null判定
  public boolean isNullOrZeroLength(String s) {
    if(s == null) {
      return true;
    }
    else if(s.length() == 0){
      return true;
    }
    else {
      return false;
    }
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
  }
}