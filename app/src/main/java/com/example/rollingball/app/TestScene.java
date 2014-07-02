package com.example.rollingball.app;
import android.opengl.GLES20;
import android.util.Log;
import android.util.TimeUtils;

import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Matrices;
import com.hackoeur.jglm.Vec3;

public class TestScene extends Scene
{

  PlayerGameObject object;

  TestScene( final SceneManager s )
  {
    super( s );
    object = new PlayerGameObject();
    object.model = ModelData.generate_cube( 1, this );
    object.position = new Vec3( 0,0,0 );
    push( object );
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

    Vec3 position = new Vec3( 0, 0, 0 );
    Mat4 translation = Mat4.MAT4_IDENTITY.translate( position );
    Mat4 rotation_x= Matrices.rotate( (float)Math.sin( delta_time_in_ns ), new Vec3( 1.0f, 0.0f, 0.0f ) );

    Mat4 scaling = new Mat4( 1.0f, 0.0f, 0.0f, 0.0f,
                             0.0f, 1.0f, 0.0f, 0.0f,
                             0.0f, 0.0f, 1.0f, 0.0f,
                             0.0f, 0.0f, 0.0f, 1.0f );

    Mat4 world_transformation = translation.multiply( rotation_x.multiply( scaling ) );
//    object.model.draw(  world_transformation  );

  }
}