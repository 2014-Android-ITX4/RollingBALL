package com.example.rollingball.app;

import android.opengl.GLES20;
import android.util.Log;

import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Vec3;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * Created by Tukasa on 2014/05/14.
 */
public class ModelData
{
  private int _vertex_array_object_id;
  private int _vertices_buffer_id;
  private int _indices_buffer_id;
  private int _texture_buffer_id;

  private int program_id;
  private int location_of_position;

  // TODO:テスト用なので後で消すなり変更するなりする
//  int[] buffer_ids;
  FloatBuffer float_buffer;
  ByteBuffer byte_buffer;

  public ModelData( float[] arg_vertices, byte[] arg_indices )
  {
    IntBuffer program_id_buffer = IntBuffer.allocate( 1 );
    GLES20.glGetIntegerv( GLES20.GL_CURRENT_PROGRAM, program_id_buffer );
    int program_id = program_id_buffer.get();

    location_of_position = GLES20.glGetAttribLocation(program_id, "position");

    make_float_VBO( arg_vertices );
    make_byte_VBO( arg_indices );
  }


  public static ModelData generate_sphere( float radius )
  {
    return null;
  }

  public static ModelData generate_cube( float arris_length )
  {
    // TODO arris_lengthの値を反映させる
    float[] vertices =
      {
        3.0f, 3.0f, 3.0f,//頂点0
        3.0f, 3.0f, -3.0f,//頂点1
        -3.0f, 3.0f, 3.0f,//頂点2
        -3.0f, 3.0f, -3.0f,//頂点3
        3.0f, -3.0f, 3.0f,//頂点4
        3.0f, -3.0f, -3.0f,//頂点5
        -3.0f, -3.0f, 3.0f,//頂点6
        -3.0f, -3.0f, -3.0f,//頂点7
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

  public void draw( final Mat4 transformation )
  {

//    test_scene.scene_manager.view.queueEvent( new Runnable() {
//        @Override
//        public void run()
//        {
//          // 頂点バッファの指定
//          GLES20.glBindBuffer( GLES20.GL_ARRAY_BUFFER, _vertices_buffer_id );
//          GLES20.glEnableVertexAttribArray( location_of_position );
//          GLES20.glVertexAttribPointer(
//            location_of_position, 4,
//                                        GLES20.GL_FLOAT, false, 0, 0  );
//          Log.d( "GL", "頂点バッファ指定 id:" + _vertices_buffer_id );
//
//          // インデックスバッファの指定
//          GLES20.glBindBuffer( GLES20.GL_ELEMENT_ARRAY_BUFFER, _indices_buffer_id );
//          Log.d( "GL", "インデックスバッファ指定 id:" + _indices_buffer_id );
//          if ( GLES20.glGetError() != GLES20.GL_NO_ERROR )
//          {
//            Log.d( "OPEN GL", String.valueOf( GLES20.glGetError() ) );
//            throw new RuntimeException(  );
//          }
//
//          // Transformation
//          IntBuffer buffer = IntBuffer.allocate( 1 );
//          GLES20.glGetIntegerv( GLES20.GL_CURRENT_PROGRAM,  buffer );
//          int id = buffer.get();
//
//          int location_of_world_transformation = GLES20.glGetUniformLocation( id , "world_transformation" );
//          GLES20.glUniformMatrix4fv(
//            location_of_world_transformation,
//            1,
//            false,
//            transformation.getBuffer()
//          );
//
//          IntBuffer int_buffer = IntBuffer.allocate( 1 );
//          Vec3 color = new Vec3( 1, 1, 1 );
//          GLES20.glGetIntegerv( GLES20.GL_CURRENT_PROGRAM,  int_buffer );
//
//          int id_diffuse = int_buffer.get();
//          int location_of_color = GLES20.glGetUniformLocation( id_diffuse , "diffuse" );
//
//          GLES20.glUniform3fv( location_of_color, 1, color.getBuffer() );
//
//          IntBuffer int_buffer2 = IntBuffer.allocate( 1 );
//          float transparent = 1;
//          GLES20.glGetIntegerv( GLES20.GL_CURRENT_PROGRAM, int_buffer2 );
//
//          int id_transparent = int_buffer2.get();
//          int location_of_transparent = GLES20.glGetUniformLocation( id_transparent , "transparent" );
//
//          GLES20.glUniform1f( location_of_transparent, transparent );
//
//          // 面0の描画
//          GLES20.glDrawElements( GLES20.GL_TRIANGLE_STRIP, 10, GLES20.GL_UNSIGNED_BYTE, 0 );
//          Log.d( "GL", "面0描画" );
//
//          // 面1の描画
//          GLES20.glDrawElements( GLES20.GL_TRIANGLE_STRIP, 4, GLES20.GL_UNSIGNED_BYTE, 10 );
//          Log.d( "GL", "面1描画" );
//
//          // 面2の描画
//          GLES20.glDrawElements( GLES20.GL_TRIANGLE_STRIP, 4, GLES20.GL_UNSIGNED_BYTE, 14 );
//          Log.d( "GL", "面2描画" );
//
//          Log.d( "OPEN GL", String.valueOf( GLES20.glGetError() ) );
//        }
//      } );

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

  }

   //float配列をVBOに変換
  private void make_float_VBO( float[] array ) {
    //float配列をFloatBufferに変換
    float_buffer= ByteBuffer.allocateDirect( array.length * 4 ).order(
      ByteOrder.nativeOrder()).asFloatBuffer();
    float_buffer.put( array ).position(0);

    //class ModelDataVertexRunnable implements Runnable
    //{
      //ModelData model_data;

      //ModelDataVertexRunnable( ModelData model )
      //{
        //model_data = model;
      //}

      //@Override
      //public synchronized void run()
      //{
        int buffer_ids[] = new int[1];
        //FloatBufferをVBOに変換
        GLES20.glGenBuffers(1,buffer_ids,0);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,buffer_ids[0]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,
                            float_buffer.capacity()*4, float_buffer,GLES20.GL_STATIC_DRAW);

         Log.d((new Throwable()).getStackTrace()[0].getClassName(), (new Throwable()).getStackTrace()[0].getFileName() + ": " + (new Throwable()).getStackTrace()[0].getLineNumber() + ":" + GLES20.glGetError());
        Log.d( "make_float_VBO buffer_id", String.valueOf( buffer_ids[ 0 ] ) );


        _vertices_buffer_id = buffer_ids[0];
      //}
    //}
    //this.test_scene.scene_manager.view.queueEvent( new ModelDataVertexRunnable( this ) );

    Log.d( "make_float_VBO buffer_id_end", String.valueOf( _vertices_buffer_id ) );
  }

  //byte配列をVBOに変換
  private void make_byte_VBO( byte[] array ) {
    //byte配列をByteBufferに変換
    byte_buffer=ByteBuffer.allocateDirect(array.length).order(
      ByteOrder.nativeOrder());
    byte_buffer.put( array ).position(0);

    //class ModelDataIndicesRunnable
      //implements Runnable
    //{
      //ModelData model_data;

      //ModelDataIndicesRunnable( ModelData model )
      //{
        //model_data = model;
      //}

      //@Override
      //public synchronized void run()
      //{
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

        _indices_buffer_id = buffer_ids[0];

      //}
    //}

    //test_scene.scene_manager.view.queueEvent( new ModelDataIndicesRunnable( this ) );

    Log.d( "make_byte_VBO buffer_id_end", String.valueOf( _indices_buffer_id ) );
  }

}