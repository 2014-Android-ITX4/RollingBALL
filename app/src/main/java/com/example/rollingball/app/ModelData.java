package com.example.rollingball.app;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.hackoeur.jglm.Mat4;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Tukasa on 2014/05/14.
 */
public class ModelData
{
  private int _vertex_array_object_id;
  private int _vertices_buffer_id;
  private int _indices_buffer_id;
  private int _texture_buffer_id;

  public static ModelData generate_sphere( double radius )
  {

    return null;
  }

  public static ModelData generate_cube( double arris_length )
  {
    float[] vertex_list = { 0.0f, 0.5f, 0.5f, -0.5f, -0.5f, -0.5f };

    return null;
  }

  public static ModelData load_from_file( String file_path )
  {
    return null;
  }

  public void draw( Mat4 trasformation )
  {

  }

  //float配列をVBOに変換
  private int make_float_VBO( float[] array ) {
    //float配列をFloatBufferに変換
    FloatBuffer fb= ByteBuffer.allocateDirect( array.length * 4 ).order(
      ByteOrder.nativeOrder()).asFloatBuffer();
    fb.put(array).position(0);

    //FloatBufferをVBOに変換
    int[] bufferIds=new int[1];
    GLES20.glGenBuffers(1,bufferIds,0);
    GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,bufferIds[0]);
    GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,
                        fb.capacity()*4,fb,GLES20.GL_STATIC_DRAW);
    return bufferIds[0];
  }

  //byte配列をVBOに変換
  private int make_byte_VBO( byte[] array ) {
    //byte配列をByteBufferに変換
    ByteBuffer bb=ByteBuffer.allocateDirect(array.length).order(
      ByteOrder.nativeOrder());
    bb.put(array).position(0);

    //ByteBufferをVBOに変換
    int[] bufferIds=new int[1];
    GLES20.glGenBuffers(1,bufferIds,0);
    GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,bufferIds[0]);
    GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER,
                        bb.capacity(),bb,GLES20.GL_STATIC_DRAW);
    return bufferIds[0];
  }
}