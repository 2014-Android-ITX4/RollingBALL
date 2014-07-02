package com.example.rollingball.app;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ByteBuffer;
import android.opengl.GLES20;
import java.util.ArrayList;
import java.nio.IntBuffer;
import android.util.Log;
import com.hackoeur.jglm.Mat4;

public class FieldGameObject extends GameObject
{
  private int _vertices_buffer_id;
  private int _indices_buffer_id;
  private int _normalBuffer_id;    //法線バッファID
  private ArrayList< ArrayList< Float > > field_planes = new ArrayList< ArrayList< Float > >();

  public FieldGameObject( float[] arg_vertices, byte[] arg_indices, float[] arg_normals )
    {
      _vertices_buffer_id = make_float_VBO( arg_vertices );
      _indices_buffer_id = make_byte_VBO( arg_indices );
      _normalBuffer_id= make_float_VBO( arg_normals );
    }

  public void generate_simple_plane( int arris_x, int arris_z )
  {
    ArrayList< Float > field_lines = new ArrayList< Float >();

    for ( int x = 0; x < arris_x; x++ )
    {
      for ( int z = 0; z < arris_z; z++ )
      {
        if ( x == 0 || x == arris_x - 1 ) field_lines.add( 1.0f );
        else if ( z == 0 || z == arris_z - 1 ) field_lines.add( 1.0f );
        else field_lines.add( 0.0f );
      }
      field_planes.add( field_lines );
      field_lines = new ArrayList< Float >();
    }
  }

  public static FieldGameObject generate_triangle( double arris_length )
  {
    float[] vertices = {
      1.0f, 1.0f, 1.0f,//頂点0
      1.0f, 1.0f, -1.0f,//頂点1
      -1.0f, 1.0f, 1.0f,//頂点2
      -1.0f, 1.0f, -1.0f,//頂点3
    };

    //法線バッファの生成
    float[] normals={
      1.0f, 1.0f, 1.0f,//頂点0
      1.0f, 1.0f,-1.0f,//頂点1
      -1.0f, 1.0f, 1.0f,//頂点2
      -1.0f, 1.0f,-1.0f,//頂点3
    };

    float div=(float)Math.sqrt(
      (1.0f*1.0f)+(1.0f*1.0f)+(1.0f*1.0f));
    for (int i=0;i<normals.length;i++) normals[i]/=div;

    //インデックスバッファの生成
    byte[] indices = {
      0, 1, 3, //面1
      0, 2, 3, //面2
    };

    FieldGameObject field_data = new FieldGameObject( vertices, indices, normals );

    return field_data;
  }

  public void draw( GLES20 gles20 )
  {
   /*for ( int i = 0; i > field_planes.size(); ++i )
    {
      // 頂点バッファの指定
      GLES20.glBindBuffer( GLES20.GL_ARRAY_BUFFER, _vertices_buffer_id );

      //法線バッファの指定
      GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, _normalBuffer_id);

      // インデックスバッファの指定
      GLES20.glBindBuffer( GLES20.GL_ARRAY_BUFFER, _indices_buffer_id );

      */

      // 面１の描画
      // GLES20.glDrawElements( GLES20.GL_TRIANGLES, 3 , GLES20.GL_UNSIGNED_BYTE, i );
      // 面２の描画
      //GLES20.glDrawElements( GLES20.GL_TRIANGLES, 3 , GLES20.GL_UNSIGNED_BYTE, i);

      //*
      IntBuffer program_id_buffer = IntBuffer.allocate( 1 );
      GLES20.glGetIntegerv( GLES20.GL_CURRENT_PROGRAM, program_id_buffer );
      int program_id = program_id_buffer.get();
      float[] vertices =
        {
          -0.5f, 0.5f,0,
          -0.5f,-0.5f,0,
          0.5f, 0.5f,0
        };
      FloatBuffer b = ByteBuffer.allocateDirect( vertices.length * 4 ).order( ByteOrder.nativeOrder()).asFloatBuffer();
      b.put(vertices).position(0);
      int location_of_position = GLES20.glGetAttribLocation( program_id, "position");
      GLES20.glVertexAttribPointer( location_of_position, 3, GLES20.GL_FLOAT, false, 0, b  );
      GLES20.glDrawArrays( GLES20.GL_TRIANGLES, 0, 3 );
      //*/

      Log.d(
        ( new Throwable() ).getStackTrace()[ 0 ].getClassName(),
        ( new Throwable() ).getStackTrace()[ 0 ].getFileName() + ": " + ( new Throwable() ).getStackTrace()[ 0 ]
          .getLineNumber()
      );
    }
  //}

  // ファイル読み込み
  public void load_from_file()
  {
    /*try {
    FileInputStream fileInputStream;
    fileInputStream = openFileInput("myfile.txt");
    byte[] readBytes = new byte[fileInputStream.available()];
    fileInputStream.read(readBytes);
    String readString = new String(readBytes);
    Log.v("readString", readString);
  } catch (FileNotFoundException e) {
  } catch (IOException e) {
  }*/}

  //float配列をVBOに変換
  private int make_float_VBO( float[] array ) {
    //float配列をFloatBufferに変換
    FloatBuffer fb= ByteBuffer.allocateDirect( array.length * 3 ).order(
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