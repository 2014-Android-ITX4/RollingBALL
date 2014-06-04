package com.example.rollingball.app;

import android.opengl.GLES20;

import com.hackoeur.jglm.Mat4;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by Tukasa on 2014/05/14.
 */
public class ModelData
{
  private int _vertex_array_object_id;
  private int _vertices_buffer_id;
  private int _indices_buffer_id;
  private int _texture_buffer_id;

  public ModelData( float[] arg_vertices, byte[] arg_indices )
  {
    _vertices_buffer_id = make_float_VBO( arg_vertices );
    _indices_buffer_id = make_byte_VBO( arg_indices );


  }


  public static ModelData generate_sphere( double radius )
  {
    return null;
  }

  public static ModelData generate_cube( double arris_length )
  {
    // TODO arris_lengthの値を反映させる
    float[] vertices =
      {
        1.0f, 1.0f, 1.0f,//頂点0
        1.0f, 1.0f, -1.0f,//頂点1
        -1.0f, 1.0f, 1.0f,//頂点2
        -1.0f, 1.0f, -1.0f,//頂点3
        1.0f, -1.0f, 1.0f,//頂点4
        1.0f, -1.0f, -1.0f,//頂点5
        -1.0f, -1.0f, 1.0f,//頂点6
        -1.0f, -1.0f, -1.0f,//頂点7
      };

    //インデックスバッファの生成
    byte[] indices =
    {
      0, 1, 2, 3, 6, 7, 4, 5, 0, 1,//面0
      1, 5, 3, 7,            //面1
      0, 2, 4, 6,            //面2
    };

     ModelData model_data = new ModelData( vertices, indices );

    return model_data;
  }

  public static ModelData load_from_file( String file_path )
  {
    return null;
  }

  public void draw( Mat4 trasformation )
  {
    // 頂点バッファの指定
    GLES20.glBindBuffer( GLES20.GL_ARRAY_BUFFER, _vertices_buffer_id );
    //GLES20.glVertexAttribPointer( GLES20.glGetAttribLocation(program,"a_Position"), 3,
    //                              GLES20.GL_FLOAT  );

    // インデックスバッファの指定
    GLES20.glBindBuffer( GLES20.GL_ARRAY_BUFFER, _indices_buffer_id );

    // 面0の描画
    GLES20.glDrawElements( GLES20.GL_TRIANGLE_STRIP, 10, GLES20.GL_UNSIGNED_BYTE, 0 );

    // 面1の描画
    GLES20.glDrawElements( GLES20.GL_TRIANGLE_STRIP, 4, GLES20.GL_UNSIGNED_BYTE, 10 );

    // 面2の描画
    GLES20.glDrawElements( GLES20.GL_TRIANGLE_STRIP, 4, GLES20.GL_UNSIGNED_BYTE, 14 );

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