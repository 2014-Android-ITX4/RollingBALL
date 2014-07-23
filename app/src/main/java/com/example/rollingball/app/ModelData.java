package com.example.rollingball.app;

import android.opengl.GLES20;
import android.util.Log;

import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Vec3;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

public class ModelData
{
  // TODO: VAO 使う必要が生じたらどうぞ
  //private int _vertex_array_object_id;

  private int _vertices_buffer_id;
  private int _indices_buffer_id;

  private int _number_of_indices;
  private int _indices_buffer_type;
  private int _polygon_mode;

  // 再開用に頂点・インデックスの値を残しておくための変数
  private float[] _vertices;
  private byte[] _indices_byte;
  private short[] _indices_short;

  public Material material = new Material();

  public ModelData( float[] arg_vertices, byte[] arg_indices, int polygon_mode )
  {
    _vertices = new float[0];
    _indices_byte = new byte[0];
    _indices_short = new short[0];

    _vertices = arg_vertices;
    _indices_byte = arg_indices;

    _vertices_buffer_id  = generate_vertex_buffer( _vertices );
    _indices_buffer_id   = generate_index_buffer( _indices_byte );
    _number_of_indices   = arg_indices.length;
    _indices_buffer_type = GLES20.GL_UNSIGNED_BYTE;
    _polygon_mode        = polygon_mode;
  }

  public ModelData( float[] arg_vertices, byte[] arg_indices )
  {
    this( arg_vertices, arg_indices, GLES20.GL_TRIANGLES );
  }

  public ModelData( float[] arg_vertices, short[] arg_indices, int polygon_mode )
  {
    _vertices = new float[0];
    _indices_byte = new byte[0];
    _indices_short = new short[0];

    _vertices = arg_vertices;
    _indices_short = arg_indices;

    _vertices_buffer_id  = generate_vertex_buffer( _vertices );
    _indices_buffer_id   = generate_index_buffer( _indices_short );
    _number_of_indices   = arg_indices.length;
    _indices_buffer_type = GLES20.GL_UNSIGNED_SHORT;
    _polygon_mode        = polygon_mode;
  }

  public ModelData( float[] arg_vertices, short[] arg_indices )
  {
    this(arg_vertices, arg_indices, GLES20.GL_TRIANGLES);
  }

  public void on_resume()
  {
    // 改めて頂点・インデックスを設定してidを取得

    _vertices_buffer_id = generate_vertex_buffer( _vertices );

    // 以前取得したインデックスがbyteかshortかを判別
    if ( _indices_byte.length != 0 )
      _indices_buffer_id = generate_index_buffer( _indices_byte );
    else
      _indices_buffer_id = generate_index_buffer( _indices_short );

//    Log.d("ModelData on_resume()", String.valueOf( GLES20.glGetError() ));

  }

  // base_index: 頂点データを放り込む最初のインデックス
  // vs: 頂点データを放り込む配列
  // position: 位置
  // normal: 法線
  // return: 次の頂点の開始インデックス
  private static int vertices_helper( int vertex_index, float[] vertices, Vec3 position, Vec3 normal )
  {
    // position
    vertices[ vertex_index++ ] = position.getX();
    vertices[ vertex_index++ ] = position.getY();
    vertices[ vertex_index++ ] = position.getZ();

    // normal
    vertices[ vertex_index++ ] = normal.getX();
    vertices[ vertex_index++ ] = normal.getY();
    vertices[ vertex_index++ ] = normal.getZ();

    return vertex_index;
  }

  private static short[] indices_helper( short index_index, short base_index, short[] indices )
  {
    // triangle 1
    indices[ index_index++ ] = (short)( base_index + 1 );
    indices[ index_index++ ] = (short)( base_index + 0 );
    indices[ index_index++ ] = (short)( base_index + 2 );

    // triangle 2
    indices[ index_index++ ] = (short)( base_index + 1 );
    indices[ index_index++ ] = (short)( base_index + 2 );
    indices[ index_index++ ] = (short)( base_index + 3 );

    base_index = ( short ) (base_index + 4);

    short[] r = new short[2];
    r[0] = index_index;
    r[1] = base_index;

    return r;
  }

  private static int vertices_helper( int vertex_index, float[] vertices, Vec3 position )
  { return vertices_helper( vertex_index, vertices, position, new Vec3( 0.0f, 1.0f, 0.0f ) ); }

  public static ModelData generate_from_field( ArrayList< ArrayList< Float > > field )
  {
    final int field_size_x = field.size();
    final int field_size_z = field.get( 0 ).size();

    final int field_area = field_size_x * field_size_z;

    final int elements_per_position = 3; // x,y,z
    final int elements_per_normal   = 3; // x,y,z
    final int elements_per_vertex   = elements_per_position + elements_per_normal;

    final int vertices_per_surface  = 4; // 1つの面に頂点は4つ
    final int surfaces_per_cell     = 6; // 床1 + 底1 + 側面壁4 = 6
    final int vertices_per_cell     = vertices_per_surface * surfaces_per_cell;
    final int vertices_per_triangle = 3; // 1つの三角に3つの頂点
    final int triangles_per_surface = 2; // 1つの面に2つの三角
    final int triangles_per_cell    = triangles_per_surface * surfaces_per_cell;
    final int number_of_vertices    = field_area * elements_per_vertex * vertices_per_cell;
    final int number_of_indices     = field_area * vertices_per_triangle * triangles_per_cell;

    float[] vertices = new float[ number_of_vertices ];
    short[] indices  = new short[ number_of_indices  ];

    int   vertex_index = 0;
    short index_index  = 0;

    Vec3[] yp_delta =
      { new Vec3( -0.5f, 0.0f, -0.5f )
      , new Vec3( +0.5f, 0.0f, -0.5f )
      , new Vec3( -0.5f, 0.0f, +0.5f )
      , new Vec3( +0.5f, 0.0f, +0.5f )
      };

    Vec3[] yn_delta =
      { new Vec3( -0.5f, 0.0f, -0.5f )
      , new Vec3( -0.5f, 0.0f, +0.5f )
      , new Vec3( +0.5f, 0.0f, -0.5f )
      , new Vec3( +0.5f, 0.0f, +0.5f )
      };

    Vec3[] xp_delta =
      { new Vec3( +0.5f, 0.0f, +0.5f )
      , new Vec3( +0.5f, 0.0f, -0.5f )
      , new Vec3( +0.5f, 0.0f, +0.5f )
      , new Vec3( +0.5f, 0.0f, -0.5f )
      };

    Vec3[] xn_delta =
      { new Vec3( -0.5f, 0.0f, -0.5f )
      , new Vec3( -0.5f, 0.0f, +0.5f )
      , new Vec3( -0.5f, 0.0f, -0.5f )
      , new Vec3( -0.5f, 0.0f, +0.5f )
      };

    Vec3[] zp_delta =
      { new Vec3( -0.5f, 0.0f, +0.5f )
      , new Vec3( +0.5f, 0.0f, +0.5f )
      , new Vec3( -0.5f, 0.0f, +0.5f )
      , new Vec3( +0.5f, 0.0f, +0.5f )
      };

    Vec3[] zn_delta =
      { new Vec3( +0.5f, 0.0f, -0.5f )
      , new Vec3( -0.5f, 0.0f, -0.5f )
      , new Vec3( +0.5f, 0.0f, -0.5f )
      , new Vec3( -0.5f, 0.0f, -0.5f )
      };

    Vec3[][] deltas =
      { yp_delta, yn_delta
      , xp_delta, xn_delta
      , zp_delta, zn_delta
      };

    Vec3 normal_yp = new Vec3(  0, +1,  0 );
    Vec3 normal_yn = new Vec3(  0, -1,  0 );
    Vec3 normal_xp = new Vec3( +1,  0,  0 );
    Vec3 normal_xn = new Vec3( -1,  0,  0 );
    Vec3 normal_zp = new Vec3(  0,  0, +1 );
    Vec3 normal_zn = new Vec3(  0,  0, -1 );

    Vec3[] normals =
      { normal_yp, normal_yn
      , normal_xp, normal_xn
      , normal_zp, normal_zn
      };

    short base_index = 0;

    for ( int x = 0; x < field_size_x; ++x )
      for ( int z = 0; z < field_size_z; ++z )
      {
        // セルの点
        Vec3 p = new Vec3( (float)x, field.get( x ).get( z ), (float)z );

        // yp --> yn --> xp --> xn --> zp --> zn
        //   p: positive as (+)
        //   n: negative as (-)
        for ( int surface_n = 0; surface_n < surfaces_per_cell; ++surface_n )
        {
          // セルの点に対する頂点の差分を面ごとに取り出す
          //   頂点は1つの面に4つあるから4回ループは回る
          for ( int delta_n = 0; delta_n < deltas[0].length; ++delta_n )
          //for ( Vec3 delta : deltas[ n ] )
          {
            Vec3 delta = deltas[ surface_n ][ delta_n ];
            Vec3 position;
            switch ( surface_n )
            { case 1: // yn
                position = p.add( delta );
                position = new Vec3( position.getX(), 0.0f, position.getZ() );
                break;
              case 2: // xp
                position = p.add( delta );
                if ( delta_n > 1 )
                  position = new Vec3( position.getX(), 0.0f, position.getZ() );
                break;
              case 3: // xn
                position = p.add( delta );
                if ( delta_n > 1 )
                  position = new Vec3( position.getX(), 0.0f, position.getZ() );
                break;
              case 4: // zp
                position = p.add( delta );
                if ( delta_n > 1 )
                  position = new Vec3( position.getX(), 0.0f, position.getZ() );
                break;
              case 5: // zn
                position = p.add( delta );
                if ( delta_n > 1 )
                  position = new Vec3( position.getX(), 0.0f, position.getZ() );
                break;
              default: // yp
                position = p.add( delta );
                break;
            }
            vertex_index = vertices_helper( vertex_index, vertices, position );
          }
          short[] r   = indices_helper( index_index, base_index, indices );
          index_index = r[0];
          base_index  = r[1];
        }
      }

    return new ModelData( vertices, indices, GLES20.GL_TRIANGLES );
  }

  public static ModelData generate_sphere( float radius, int rings, int sectors )
  {
    final int elements_per_position = 3; // x,y,z
    final int elements_per_normal   = 3; // x,y,z
    final int elements_per_vertex   = elements_per_position + elements_per_normal;

    final int   number_of_vertices = rings * sectors * elements_per_vertex;
    final short number_of_indices  = (short)( rings * sectors * 6 );

    float[] vertices = new float[ number_of_vertices ];
    short[] indices  = new short[ number_of_indices  ];

    int   vertex_index = 0;
    short index_index  = 0;

    final float ring_step   = 1.0f / (float)( rings   - 1 );
    final float sector_step = 1.0f / (float)( sectors - 1 );
    
    final float pi = (float)Math.PI;

    for( int r = 0; r < rings; ++r )
    {
      final float rf = (float)r;
      for ( int s = 0; s < sectors; ++s )
      {
        final float sf = (float)s;

        final Vec3 v = new Vec3
          ( ( float ) ( Math.cos( 2.0f * pi * sf * sector_step ) * Math.sin( pi * rf * ring_step ) )
          , ( float ) ( Math.sin( pi * ( rf * ring_step - 0.5f ) ) )
          , ( float ) ( Math.sin( 2.0f * pi * sf * sector_step ) * Math.sin( pi * rf * ring_step ) )
          );

        // position
        vertices[ vertex_index++ ] = v.getX() * radius;
        vertices[ vertex_index++ ] = v.getY() * radius;
        vertices[ vertex_index++ ] = v.getZ() * radius;

        // normal
        vertices[ vertex_index++ ] = v.getX();
        vertices[ vertex_index++ ] = v.getY();
        vertices[ vertex_index++ ] = v.getZ();

        // TODO: テクスチャーUV座標を頂点構造に入れる事になったらどうぞ
        //vertices[vertex_index++] = s * sector_step;
        //vertices[vertex_index++] = r * ring_step;
      }
    }

    for ( short r = 0; r < rings - 1; ++r )
      for ( short s = 0; s < sectors - 1; ++s )
      {
        short r1 = (short)(r + 1);
        short s1 = (short)(s + 1);

        short i0 = (short)( r  * sectors + s  );
        short i1 = (short)( r  * sectors + s1 );
        short i2 = (short)( r1 * sectors + s1 );
        short i3 = (short)( r1 * sectors + s  );

        indices[index_index++] = i1;
        indices[index_index++] = i0;
        indices[index_index++] = i2;

        indices[index_index++] = i0;
        indices[index_index++] = i2;
        indices[index_index++] = i3;
      }

    return new ModelData( vertices, indices, GLES20.GL_TRIANGLE_STRIP );
  }

  public static ModelData generate_sphere( float radius, int split )
  { return generate_sphere( radius, split, split ); }

  public static ModelData generate_sphere( float radius )
  { return generate_sphere( radius, 16 ); }

  public static ModelData generate_sphere( )
  { return generate_sphere( 0.5f ); }

  public static ModelData generate_cube( float arris_length )
  {
    float vl = arris_length * 0.5f;

    float[] vertices =
    {
      +vl, +vl, +vl, +vl, +vl, +vl, //頂点0
      +vl, +vl, -vl, +vl, +vl, -vl,//頂点1
      -vl, +vl, +vl, -vl, +vl, +vl,//頂点2
      -vl, +vl, -vl, -vl, +vl, -vl,//頂点3
      +vl, -vl, +vl, +vl, -vl, +vl,//頂点4
      +vl, -vl, -vl, +vl, -vl, -vl,//頂点5
      -vl, -vl, +vl, -vl, -vl, +vl,//頂点6
      -vl, -vl, -vl, vl, -vl, -vl,//頂点7
    };

    //インデックスバッファーの元データ配列を定義
    byte[] indices =
    {
      0, 1, 2, 3, 6, 7, 4, 5, 0, 1,//面0
      1, 5, 3, 7,            //面1
      0, 2, 4, 6            //面2
    };

    return new ModelData( vertices, indices );
  }

  public static ModelData generate_board( float arris_length )
  {
    float vl = arris_length * 0.5f;

    float[] vertices =
      { -vl, +vl, 0.0f, 0.0f, 0.0f, 1.0f
      , -vl, -vl, 0.0f, 0.0f, 0.0f, 1.0f
      , +vl, +vl, 0.0f, 0.0f, 0.0f, 1.0f
      , +vl, -vl, 0.0f, 0.0f, 0.0f, 1.0f
      };

    //インデックスバッファーの元データ配列を定義
    byte[] indices = { 0, 1, 2, 2, 1, 3 };

    return new ModelData( vertices, indices );
  }

  public static ModelData generate_cube( )
  { return generate_cube( 1.0f ); }

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

    final int size_of_float    = 4;
    final int offset_of_normal = 3;
    final int position_stride  = 0;
    final int normal_stride    = size_of_float * offset_of_normal;
    final int elements_per_position = 3;
    final int elements_per_normal   = 3;
    final int elements_per_vertex   = elements_per_position + elements_per_normal;
    final int vertex_size           = size_of_float * elements_per_vertex;

    // 頂点レイアウトの指定
    GLES20.glVertexAttribPointer( GLES20.glGetAttribLocation( program_id, "position" ), 3, GLES20.GL_FLOAT, false, vertex_size, position_stride );

    // 頂点レイアウトの指定
    GLES20.glVertexAttribPointer( GLES20.glGetAttribLocation( program_id, "normal" ), 3, GLES20.GL_FLOAT, false, vertex_size, normal_stride );

    // ワールド変換
    GLES20.glUniformMatrix4fv( GLES20.glGetUniformLocation( program_id , "world_transformation" ), 1, false, transformation.getBuffer( ) );

    // 材質の適用
    material.draw( program_id );

    // 面群の描画
    GLES20.glDrawElements( _polygon_mode, _number_of_indices, _indices_buffer_type, 0 );

    // 束縛解除
    GLES20.glBindBuffer( GLES20.GL_ARRAY_BUFFER, 0 );
    GLES20.glBindBuffer( GLES20.GL_ELEMENT_ARRAY_BUFFER, 0 );

//    Log.d( "ModelData draw", String.valueOf( GLES20.glGetError() ) );
    //Log.d( "GL_NO_ERROR", String.valueOf( GLES20.glGetError() == GLES20.GL_NO_ERROR ) );
  }

  // float[] --> FloatBuffer
  private static FloatBuffer create_buffer( float[] float_array )
  {
    FloatBuffer b = ByteBuffer.allocateDirect( float_array.length * 4 ).order( ByteOrder.nativeOrder() ).asFloatBuffer( );
    b.put( float_array ).position( 0 );
    return b;
  }

  // FloatBuffer --> int ( generated id of vertex buffer object )
  private static int generate_vertex_buffer( FloatBuffer b )
  {
    // VBO をGPUに生成し、CPUの FloatBuffer を GPU の VBO へ転送
    int buffer_ids[] = new int[ 1 ];

    GLES20.glGenBuffers( 1, buffer_ids, 0 );
    GLES20.glBindBuffer( GLES20.GL_ARRAY_BUFFER, buffer_ids[ 0 ] );
    GLES20.glBufferData( GLES20.GL_ARRAY_BUFFER, b.capacity() * 4, b, GLES20.GL_STATIC_DRAW );
    GLES20.glBindBuffer( GLES20.GL_ARRAY_BUFFER, 0 );

    return buffer_ids[ 0 ];
  }

  private static int generate_vertex_buffer( float[] float_array )
  { return generate_vertex_buffer( create_buffer( float_array ) ); }

  // byte[] --> ByteBuffer
  private static ByteBuffer create_buffer( byte[] byte_array )
  {
    ByteBuffer b = ByteBuffer.allocate( byte_array.length ).order( ByteOrder.nativeOrder( ) );
    b.put( byte_array ).position( 0 );
    return b;
  }

  // ByteBuffer --> int ( generated id of vertex buffer object )
  private static int generate_index_buffer( ByteBuffer b )
  {
    int[] buffer_ids = new int[ 1 ];

    GLES20.glGenBuffers( 1, buffer_ids, 0 );
    GLES20.glBindBuffer( GLES20.GL_ELEMENT_ARRAY_BUFFER, buffer_ids[ 0 ] );
    GLES20.glBufferData( GLES20.GL_ELEMENT_ARRAY_BUFFER, b.capacity( ), b, GLES20.GL_STATIC_DRAW );
    GLES20.glBindBuffer( GLES20.GL_ELEMENT_ARRAY_BUFFER, 0 );

    return buffer_ids[ 0 ];
  }

  private static int generate_index_buffer( byte[] byte_array )
  { return generate_index_buffer( create_buffer( byte_array ) );}

  // short[] --> ShortBuffer
  private static ShortBuffer create_buffer( short[] short_array )
  {
    ShortBuffer b = ByteBuffer.allocateDirect( short_array.length * 2 ).order( ByteOrder.nativeOrder( ) ).asShortBuffer();
    b.put( short_array ).position( 0 );
    return b;
  }

  // ByteBuffer --> int ( generated id of vertex buffer object )
  private static int generate_index_buffer( ShortBuffer b )
  {
    int[] buffer_ids = new int[ 1 ];

    GLES20.glGenBuffers( 1, buffer_ids, 0 );
    GLES20.glBindBuffer( GLES20.GL_ELEMENT_ARRAY_BUFFER, buffer_ids[ 0 ] );
    GLES20.glBufferData( GLES20.GL_ELEMENT_ARRAY_BUFFER, b.capacity( ) * 2, b, GLES20.GL_STATIC_DRAW );
    GLES20.glBindBuffer( GLES20.GL_ELEMENT_ARRAY_BUFFER, 0 );

    return buffer_ids[ 0 ];
  }

  private static int generate_index_buffer( short[] short_array )
  { return generate_index_buffer( create_buffer( short_array ) ); }

}