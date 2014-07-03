package com.example.rollingball.app;

import android.opengl.GLES20;
import android.util.Log;

import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Vec3;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class ModelData
{
  private int _vertex_array_object_id;
  private int _vertices_buffer_id;
  private int _indices_buffer_id;
  private int _texture_buffer_id;

  // TODO:テスト用なので後で消すなり変更するなりする
  FloatBuffer _float_buffer;
  ByteBuffer  _byte_buffer;

  public ModelData( float[] arg_vertices, byte[] arg_indices )
  {
    make_float_VBO( arg_vertices );
    make_byte_VBO( arg_indices );
  }

  public static ModelData generate_sphere( float radius )
  {
    // TODO
    throw new NotImplementedException();
  }

  public static ModelData generate_cube( float arris_length )
  {
    float vl = arris_length * 0.5f;

    float[] vertices =
    {
      +vl, +vl, +vl,//頂点0
      +vl, +vl, -vl,//頂点1
      -vl, +vl, +vl,//頂点2
      -vl, +vl, -vl,//頂点3
      +vl, -vl, +vl,//頂点4
      +vl, -vl, -vl,//頂点5
      -vl, -vl, +vl,//頂点6
      -vl, -vl, -vl,//頂点7
    };

    //インデックスバッファの元データ配列を定義
    byte[] indices =
    {
      0, 1, 2, 3, 6, 7, 4, 5, 0, 1,//面0
      1, 5, 3, 7,            //面1
      0, 2, 4, 6,            //面2
    };

    return new ModelData( vertices, indices );
  }

  public static ModelData load_from_file( String file_path )
  {
    // TODO
    throw new NotImplementedException();
  }

  public void draw( final Mat4 transformation )
  {
    IntBuffer program_id_buffer = IntBuffer.allocate( 1 );
    GLES20.glGetIntegerv( GLES20.GL_CURRENT_PROGRAM, program_id_buffer );
    int program_id = program_id_buffer.get();

    int location_of_position = GLES20.glGetAttribLocation( program_id, "position" );

    // 頂点バッファの束縛
    GLES20.glBindBuffer( GLES20.GL_ARRAY_BUFFER, _vertices_buffer_id );
    //GLES20.glEnableVertexAttribArray( location_of_position );
    GLES20.glVertexAttribPointer( location_of_position, 4, GLES20.GL_FLOAT, false, 0, 0  );
    //Log.d( "GL", "頂点バッファ指定 id:" + _vertices_buffer_id );

    // インデックスバッファの束縛
    GLES20.glBindBuffer( GLES20.GL_ELEMENT_ARRAY_BUFFER, _indices_buffer_id );
    //Log.d( "GL", "インデックスバッファ指定 id:" + _indices_buffer_id );

    // ワールド変換
    int location_of_world_transformation = GLES20.glGetUniformLocation( program_id , "world_transformation" );
    GLES20.glUniformMatrix4fv( location_of_world_transformation, 1, false, transformation.getBuffer() );

    // 拡散反射光の色
    int location_of_diffuse = GLES20.glGetUniformLocation( program_id, "diffuse" );
    GLES20.glUniform3fv( location_of_diffuse, 1, new Vec3(0.0f, 1.0f, 0.0f).getBuffer() );

    // 不透明度
    int location_of_transparent = GLES20.glGetUniformLocation( program_id, "transparent" );
    GLES20.glUniform1f( location_of_transparent, 1.0f );

    // TODO: テクスチャー対応用。テクスチャーに対応する際にどうぞ
    // テクスチャーのブレンド比
    //int location_of_diffuse_texture_blending_factor = GLES20.glGetUniformLocation( program_id, "location_of_diffuse_texture_blending_factor" );
    //GLES20.glUniform1f( location_of_diffuse_texture_blending_factor, 0.0f );

    // 面群の描画
    GLES20.glDrawElements( GLES20.GL_TRIANGLE_STRIP, 10, GLES20.GL_UNSIGNED_BYTE, 0 );
    GLES20.glDrawElements( GLES20.GL_TRIANGLE_STRIP, 4, GLES20.GL_UNSIGNED_BYTE, 10 );
    GLES20.glDrawElements( GLES20.GL_TRIANGLE_STRIP, 4, GLES20.GL_UNSIGNED_BYTE, 14 );

    // 束縛解除
    GLES20.glBindBuffer( GLES20.GL_ARRAY_BUFFER, 0 );
    GLES20.glBindBuffer( GLES20.GL_ELEMENT_ARRAY_BUFFER, 0 );

    //Log.d( "GL_NO_ERROR", String.valueOf( GLES20.glGetError() == GLES20.GL_NO_ERROR ) );
  }

  // float[] --> int ( generated id of vertex buffer object )
  private void make_float_VBO( float[] array )
  {
    _float_buffer = ByteBuffer.allocateDirect( array.length * 4 ).order( ByteOrder.nativeOrder()).asFloatBuffer();
    _float_buffer.put( array ).position(0);

    // VBO をGPUに生成し、CPUの FloatBuffer を GPU の VBO へ転送
    int buffer_ids[] = new int[1];
    GLES20.glGenBuffers(1,buffer_ids,0);
    Log.d( "GL_NO_ERROR1", String.valueOf( GLES20.glGetError() == GLES20.GL_NO_ERROR ) );

    GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffer_ids[0]);
    Log.d( "GL_NO_ERROR2", String.valueOf( GLES20.glGetError() == GLES20.GL_NO_ERROR ) );

    GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, _float_buffer.capacity() * 4, _float_buffer,GLES20.GL_STATIC_DRAW);
    Log.d( "GL_NO_ERROR3", String.valueOf( GLES20.glGetError() == GLES20.GL_NO_ERROR ) );

    _vertices_buffer_id = buffer_ids[0];

    Log.d( "make_float_VBO buffer_id_end", String.valueOf( _vertices_buffer_id ) );
  }

  // byte[] --> int ( generated id of vertex buffer object )
  private void make_byte_VBO( byte[] array )
  {
    _byte_buffer = ByteBuffer.allocateDirect(array.length).order( ByteOrder.nativeOrder());
    _byte_buffer.put( array ).position(0);

    int[] buffer_ids=new int[1];

    GLES20.glGenBuffers(1,buffer_ids,0);
    Log.d( "GL_NO_ERROR4", String.valueOf( GLES20.glGetError() == GLES20.GL_NO_ERROR ) );
    GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,buffer_ids[0]);
    Log.d( "GL_NO_ERROR5", String.valueOf( GLES20.glGetError() == GLES20.GL_NO_ERROR ) );
    GLES20.glBufferData( GLES20.GL_ELEMENT_ARRAY_BUFFER, _byte_buffer.capacity(), _byte_buffer, GLES20.GL_STATIC_DRAW);
    Log.d( "GL_NO_ERROR6", String.valueOf( GLES20.glGetError() == GLES20.GL_NO_ERROR ) );

    _indices_buffer_id = buffer_ids[0];

    Log.d( "make_byte_VBO buffer_id_end", String.valueOf( _indices_buffer_id ) );
  }

}