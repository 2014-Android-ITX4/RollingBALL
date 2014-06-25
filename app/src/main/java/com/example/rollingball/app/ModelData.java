package com.example.rollingball.app;

import android.opengl.GLES20;
import android.util.Log;

import com.hackoeur.jglm.Mat4;

import junit.framework.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * Created by Tukasa on 2014/05/14.
 */
public class ModelData
{
  private int _vertex_array_object_id;
  private int _vertices_buffer_id;
  private int _indices_buffer_id;
  private int _texture_buffer_id;

  // TODO:テスト用なので後で消すなり変更するなりする
  TestScene test_scene;
//  int[] buffer_ids;
  FloatBuffer float_buffer;
  ByteBuffer byte_buffer;

  public ModelData( float[] arg_vertices, byte[] arg_indices, TestScene arg_test_scene )
  {
    test_scene = arg_test_scene;

    _vertices_buffer_id = make_float_VBO( arg_vertices );
    _indices_buffer_id = make_byte_VBO( arg_indices );
  }


  public static ModelData generate_sphere( double radius )
  {
    return null;
  }

  public static ModelData generate_cube( double arris_length, TestScene arg_test_scene )
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



     ModelData model_data = new ModelData( vertices, indices, arg_test_scene );

    return model_data;
  }

  public static ModelData load_from_file( String file_path )
  {
    return null;
  }

  public void draw( Mat4 trasformation )
  {

    test_scene.scene_manager.view.queueEvent( new Runnable() {
        @Override
        public void run()
        {
          int program = test_scene.scene_manager.view.renderer.program;

          // 頂点バッファの指定
          GLES20.glBindBuffer( GLES20.GL_ARRAY_BUFFER, _vertices_buffer_id );
          GLES20.glVertexAttribPointer( GLES20.glGetAttribLocation(program,"position"), 4,
                                        GLES20.GL_FLOAT, false, 0, 0  );
          Log.d( "GL", "頂点バッファ指定 id:" + _vertices_buffer_id );

          // インデックスバッファの指定
          GLES20.glBindBuffer( GLES20.GL_ELEMENT_ARRAY_BUFFER, _indices_buffer_id );
          Log.d( "GL", "インデックスバッファ指定 id:" + _indices_buffer_id );
          if ( GLES20.glGetError() != GLES20.GL_NO_ERROR )
          {
            Log.d( "OPEN GL", String.valueOf( GLES20.glGetError() ) );
            throw new RuntimeException(  );
          }


          // 面0の描画
          GLES20.glDrawElements( GLES20.GL_TRIANGLE_STRIP, 10, GLES20.GL_UNSIGNED_BYTE, 0 );
          Log.d( "GL", "面0成功" );


          // 面1の描画
          GLES20.glDrawElements( GLES20.GL_TRIANGLE_STRIP, 4, GLES20.GL_UNSIGNED_BYTE, 10 );
          Log.d( "GL", "面1成功" );

          // 面2の描画
          GLES20.glDrawElements( GLES20.GL_TRIANGLE_STRIP, 4, GLES20.GL_UNSIGNED_BYTE, 14 );
          Log.d( "GL", "面2成功" );

          Log.d( "OPEN GL", String.valueOf( GLES20.glGetError() ) );
        }
      } );



  }

  //

  //float配列をVBOに変換
  private int make_float_VBO( float[] array ) {
    //float配列をFloatBufferに変換
    float_buffer= ByteBuffer.allocateDirect( array.length * 4 ).order(
      ByteOrder.nativeOrder()).asFloatBuffer();
    float_buffer.put( array ).position(0);

    FutureTask<int[]> future_task = new FutureTask<int[]>( new Callable() {
      @Override
      public int[] call()
        throws Exception
      {
        int buffer_ids[] = new int[1];
        //FloatBufferをVBOに変換
        GLES20.glGenBuffers(1,buffer_ids,0);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,buffer_ids[0]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,
                            float_buffer.capacity()*4,float_buffer,GLES20.GL_STATIC_DRAW);

        Log.d( "OPEN GL", String.valueOf( GLES20.glGetError() ) );
        Log.d( "make_float_VBO buffer_id", String.valueOf( buffer_ids[ 0 ] ) );

        return buffer_ids;
      }
    } );
    this.test_scene.scene_manager.view.queueEvent( future_task );

    int[] result;

    try
    {
     result = future_task.get();
    }catch (Exception e )
    {
      Log.d( "futureTask","エラー" );
      result = new int[1];
      result[0] = -1;
    }


//    this.test_scene.scene_manager.view.queueEvent( new Runnable() {
//      @Override
//      public void run()
//      {
//        //FloatBufferをVBOに変換
//        GLES20.glGenBuffers(1,buffer_ids,0);
//        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,buffer_ids[0]);
//        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,
//                            float_buffer.capacity()*4,float_buffer,GLES20.GL_STATIC_DRAW);
//
//        Log.d( "OPEN GL", String.valueOf( GLES20.glGetError() ) );
//        Log.d( "make_float_VBO buffer_id", String.valueOf( buffer_ids[ 0 ] ) );
//      }
//    } );

    Log.d( "make_float_VBO buffer_id2", String.valueOf( result[ 0 ] ) );
    return result[0];
//    return 1;
  }

  //byte配列をVBOに変換
  private int make_byte_VBO( byte[] array ) {
    //byte配列をByteBufferに変換
    byte_buffer=ByteBuffer.allocateDirect(array.length).order(
      ByteOrder.nativeOrder());
    byte_buffer.put( array ).position(0);



    FutureTask<int[]> future_task = new FutureTask<int[]>( new Callable() {
      @Override
      public int[] call()
        throws Exception
      {
        int[] buffer_ids=new int[1];

        GLES20.glGenBuffers(1,buffer_ids,0);
        Log.d( "OPEN GL", String.valueOf( GLES20.glGetError() ) );
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,buffer_ids[0]);
        Log.d( "OPEN GL", String.valueOf( GLES20.glGetError() ) );
        GLES20.glBufferData(
          GLES20.GL_ELEMENT_ARRAY_BUFFER, byte_buffer.capacity(), byte_buffer, GLES20.GL_STATIC_DRAW
        );
        Log.d( "OPEN GL", String.valueOf( GLES20.glGetError() ) );
        Log.d( "make_byte_VBO buffer_id", String.valueOf( buffer_ids[ 0 ] ) );

        return buffer_ids;
      }
    } );
    test_scene.scene_manager.view.queueEvent( future_task );

    int[] result;
    try
    {
      result = future_task.get();
    }catch (Exception e )
    {
      Log.d( "future_task","エラー" );
      result = new int[1];
      result[0] = -1;
    }

//    test_scene.scene_manager.view.queueEvent( new Runnable() {
//      @Override
//      public void run()
//      {
//        //ByteBufferをVBOに変換
//
//        GLES20.glGenBuffers(1,buffer_ids,0);
//        Log.d( "OPEN GL", String.valueOf( GLES20.glGetError() ) );
//        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,buffer_ids[0]);
//        Log.d( "OPEN GL", String.valueOf( GLES20.glGetError() ) );
//        GLES20.glBufferData(
//          GLES20.GL_ELEMENT_ARRAY_BUFFER, byte_buffer.capacity(), byte_buffer, GLES20.GL_STATIC_DRAW
//        );
//        Log.d( "OPEN GL", String.valueOf( GLES20.glGetError() ) );
//        Log.d( "make_byte_VBO buffer_id", String.valueOf( buffer_ids[ 0 ] ) );
//      }
//
//    } );


    Log.d( "make_byte_VBO buffer_id2", String.valueOf( result[ 0 ] ) );
    return result[0];
//    return 2;
  }
}