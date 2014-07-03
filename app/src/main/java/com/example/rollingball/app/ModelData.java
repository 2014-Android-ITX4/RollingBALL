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

  public ModelData( float[] arg_vertices, byte[] arg_indices )
  {
    _vertices_buffer_id = generate_vertex_buffer_from_float_buffer( create_float_buffer( arg_vertices ) );
    //Log.d( "_vertices_buffer_id generated", String.valueOf( _vertices_buffer_id ) );

    _indices_buffer_id = generate_index_buffer_from_byte_buffer( create_byte_buffer( arg_indices ) );
    //Log.d( "_indices_buffer_id generated", String.valueOf( _indices_buffer_id ) );
  }

  public static ModelData generate_sphere( float radius )
  {
    // TODO: #151
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
    // TODO: #152
    throw new NotImplementedException();
  }

  public void draw( final Mat4 transformation )
  {
    // シェーダープログラムの取得
    IntBuffer program_id_buffer = IntBuffer.allocate( 1 );
    GLES20.glGetIntegerv( GLES20.GL_CURRENT_PROGRAM, program_id_buffer );
    int program_id = program_id_buffer.get();

    // 頂点バッファの束縛
    GLES20.glBindBuffer( GLES20.GL_ARRAY_BUFFER, _vertices_buffer_id );

    // インデックスバッファの束縛
    GLES20.glBindBuffer( GLES20.GL_ELEMENT_ARRAY_BUFFER, _indices_buffer_id );

    // 頂点レイアウトの指定
    GLES20.glVertexAttribPointer( GLES20.glGetAttribLocation( program_id, "position" ), 4, GLES20.GL_FLOAT, false, 0, 0  );

    // ワールド変換
    GLES20.glUniformMatrix4fv( GLES20.glGetUniformLocation( program_id , "world_transformation" ), 1, false, transformation.getBuffer( ) );

    // 拡散反射光の色
    GLES20.glUniform3fv( GLES20.glGetUniformLocation( program_id, "diffuse" ), 1, new Vec3( 0.0f, 1.0f, 0.0f ).getBuffer( ) );

    // 不透明度
    GLES20.glUniform1f( GLES20.glGetUniformLocation( program_id, "transparent" ), 1.0f );

    // TODO: テクスチャー対応用。テクスチャーに対応する際にどうぞ
    // テクスチャーのブレンド比
    //GLES20.glUniform1f( GLES20.glGetUniformLocation( program_id, "location_of_diffuse_texture_blending_factor" ), 0.0f );

    // 面群の描画
    GLES20.glDrawElements( GLES20.GL_TRIANGLE_STRIP, 10, GLES20.GL_UNSIGNED_BYTE, 0 );
    GLES20.glDrawElements( GLES20.GL_TRIANGLE_STRIP, 4, GLES20.GL_UNSIGNED_BYTE, 10 );
    GLES20.glDrawElements( GLES20.GL_TRIANGLE_STRIP, 4, GLES20.GL_UNSIGNED_BYTE, 14 );

    // 束縛解除
    GLES20.glBindBuffer( GLES20.GL_ARRAY_BUFFER, 0 );
    GLES20.glBindBuffer( GLES20.GL_ELEMENT_ARRAY_BUFFER, 0 );

    //Log.d( "GL_NO_ERROR", String.valueOf( GLES20.glGetError() == GLES20.GL_NO_ERROR ) );
  }

  // float[] --> FloatBuffer
  private static FloatBuffer create_float_buffer( float[] float_array )
  {
    FloatBuffer b = ByteBuffer.allocateDirect( float_array.length * 4 ).order( ByteOrder.nativeOrder() ).asFloatBuffer( );
    b.put( float_array ).position( 0 );
    return b;
  }

  // FloatBuffer --> int ( generated id of vertex buffer object )
  private static int generate_vertex_buffer_from_float_buffer( FloatBuffer b )
  {
    // VBO をGPUに生成し、CPUの FloatBuffer を GPU の VBO へ転送
    int buffer_ids[] = new int[ 1 ];

    GLES20.glGenBuffers( 1, buffer_ids, 0 );
    GLES20.glBindBuffer( GLES20.GL_ARRAY_BUFFER, buffer_ids[ 0 ] );
    GLES20.glBufferData( GLES20.GL_ARRAY_BUFFER, b.capacity() * 4, b, GLES20.GL_STATIC_DRAW );

    return buffer_ids[ 0 ];
  }

  // byte[] --> ByteBuffer
  private static ByteBuffer create_byte_buffer( byte[] byte_array )
  {
    ByteBuffer b = ByteBuffer.allocate( byte_array.length ).order( ByteOrder.nativeOrder( ) );
    b.put( byte_array ).position( 0 );
    return b;
  }

  // ByteBuffer --> int ( generated id of vertex buffer object )
  private static int generate_index_buffer_from_byte_buffer( ByteBuffer b )
  {
    int[] buffer_ids=new int[ 1 ];

    GLES20.glGenBuffers( 1, buffer_ids, 0 );
    GLES20.glBindBuffer( GLES20.GL_ELEMENT_ARRAY_BUFFER,buffer_ids[ 0 ] );
    GLES20.glBufferData( GLES20.GL_ELEMENT_ARRAY_BUFFER, b.capacity( ), b, GLES20.GL_STATIC_DRAW );

    return buffer_ids[ 0 ];
  }

}