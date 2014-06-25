package com.example.rollingball.app;

import java.nio.IntBuffer;
import android.opengl.GLES20;
import com.hackoeur.jglm.Matrices;
import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Vec3;

/**
 * Created by Tukasa on 2014/05/14.
 */
public class Camera implements IUpdatable
{
  public Vec3 eye= new Vec3( 0, 0, 10 );
  public Vec3 look_at = new Vec3( 0, 0, 0 );
  public Vec3 up = new Vec3( 0, 1, 0 );
  public Scene scene;

  public void Camera( final Scene scene_ )
  {
    scene = scene_;
  }

  @Override
  public void update( final long delta_time_in_ns )
  {
    IntBuffer buffer = IntBuffer.allocate( 1 );
    Mat4 view = Matrices.lookAt( eye, look_at, up );
    GLES20.glGetIntegerv( GLES20.GL_CURRENT_PROGRAM,  buffer );

    int id = buffer.get();
    int location_of_view_transformation = GLES20.glGetUniformLocation( id , "view_transformation" );

    GLES20.glUniformMatrix4fv( location_of_view_transformation, 1, false, view.getBuffer() );
  }
}
