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

  // TODO: Texture 使う必要が生じたらどうぞ
  //private int _texture_buffer_id;

  public ModelData( float[] arg_vertices, byte[] arg_indices, int polygon_mode )
  {
    _vertices_buffer_id  = generate_vertex_buffer( arg_vertices );
    _indices_buffer_id   = generate_index_buffer( arg_indices );
    _number_of_indices   = arg_indices.length;
    _indices_buffer_type = GLES20.GL_UNSIGNED_BYTE;
    _polygon_mode        = polygon_mode;
  }

  public ModelData( float[] arg_vertices, byte[] arg_indices )
  {
    this( arg_vertices, arg_indices, GLES20.GL_TRIANGLE_STRIP );
  }

  public ModelData( float[] arg_vertices, short[] arg_indices, int polygon_mode )
  {
    _vertices_buffer_id  = generate_vertex_buffer( arg_vertices );
    _indices_buffer_id   = generate_index_buffer( arg_indices );
    _number_of_indices   = arg_indices.length;
    _indices_buffer_type = GLES20.GL_UNSIGNED_SHORT;
    _polygon_mode        = polygon_mode;
  }

  public ModelData( float[] arg_vertices, short[] arg_indices )
  {
    this(arg_vertices, arg_indices, GLES20.GL_TRIANGLE_STRIP);
  }

  public static ModelData generate_from_field( ArrayList< ArrayList< Float > > field )
  {
    final int field_size_x = field.size();
    final int field_size_z = field.get( 0 ).size();

    final int field_area = field_size_x * field_size_z;

    final int elements_per_vertices = 3;
    final int vertices_per_cell     = 4;
    final int vertices_per_triangle = 3;
    final int triangles_per_cell    = 2;
    final int number_of_vertices = field_area * elements_per_vertices * vertices_per_cell;
    final int number_of_indices  = field_area * vertices_per_triangle * triangles_per_cell;

    float[] vertices = new float[ number_of_vertices ];
    short[] indices  = new short[ number_of_indices  ];

    int   vertex_index = 0;
    short index_index  = 0;

    Vec3[] ds =
      { new Vec3( -0.5f, -0.5f, 0.0f )
      , new Vec3( +0.5f, -0.5f, 0.0f )
      , new Vec3( -0.5f, +0.5f, 0.0f )
      , new Vec3( +0.5f, +0.5f, 0.0f )
      };

    for ( int x = 0; x < field_size_x; ++x )
      for ( int z = 0; z < field_size_z; ++z )
      {
        Vec3 p = new Vec3( (float)x, (float)z, field.get( x ).get( z ) );
        for ( Vec3 d : ds )
        {
          Vec3 v = p.add( d );

          vertices[ vertex_index++ ] = v.getX();
          vertices[ vertex_index++ ] = v.getY();
          vertices[ vertex_index++ ] = v.getZ();
        }

        short base_index = index_index;

        indices[ index_index++ ] = (short)( base_index + 0 );
        indices[ index_index++ ] = (short)( base_index + 1 );
        indices[ index_index++ ] = (short)( base_index + 2 );

        indices[ index_index++ ] = (short)( base_index + 2 );
        indices[ index_index++ ] = (short)( base_index + 1 );
        indices[ index_index++ ] = (short)( base_index + 3 );
      }

    return new ModelData( vertices, indices, GLES20.GL_TRIANGLES );
  }

  public static ModelData generate_sphere( float radius, int rings, int sectors )
  {
    int   number_of_vertices = rings * sectors * 3;
    short number_of_indices  = (short)( rings * sectors * 6 );

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
          , ( float ) ( Math.sin( pi * ( 0.5f + rf * ring_step - 1.0f ) ) )
          , ( float ) ( Math.sin( 2.0f * pi * sf * sector_step ) * Math.sin( pi * rf * ring_step ) )
          );

        vertices[ vertex_index++ ] = v.getX() * radius;
        vertices[ vertex_index++ ] = v.getY() * radius;
        vertices[ vertex_index++ ] = v.getZ() * radius;

        // TODO: テクスチャーUV座標を頂点構造に入れる事になったらどうぞ
        //vertices[vertex_index++] = s * sector_step;
        //vertices[vertex_index++] = r * ring_step;

        // TODO: 法線ベクターを頂点構造に入れる事になったらどうぞ
        //vertices[vertex_index++] = v.getX();
        //vertices[vertex_index++] = v.getY();
        //vertices[vertex_index++] = v.getZ();
      }
    }

    for ( short r = 0; r < rings - 1; ++r )
      for ( short s = 0; s < sectors - 1; ++s )
      {
        short r1 = (short)(r + 1);
        short s1 = (short)(s + 1);

        /*
        indices[index_index++] = (short)( r  * sectors + s  );
        indices[index_index++] = (short)( r  * sectors + s1 );
        indices[index_index++] = (short)( r1 * sectors + s1 );
        indices[index_index++] = (short)( r1 * sectors + s  );
        */

        short i0 = (short)( r  * sectors + s  );
        short i1 = (short)( r  * sectors + s1 );
        short i2 = (short)( r1 * sectors + s1 );
        short i3 = (short)( r1 * sectors + s  );

        indices[index_index++] = i0;
        indices[index_index++] = i1;
        indices[index_index++] = i2;

        indices[index_index++] = i0;
        indices[index_index++] = i2;
        indices[index_index++] = i3;
      }

    return new ModelData( vertices, indices, GLES20.GL_TRIANGLES );
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
      0, 2, 4, 6            //面2
    };

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

    // 頂点レイアウトの指定
    GLES20.glVertexAttribPointer( GLES20.glGetAttribLocation( program_id, "position" ), 3, GLES20.GL_FLOAT, false, 0, 0  );

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
    GLES20.glDrawElements( _polygon_mode, _number_of_indices, _indices_buffer_type, 0 );

    // 束縛解除
    GLES20.glBindBuffer( GLES20.GL_ARRAY_BUFFER, 0 );
    GLES20.glBindBuffer( GLES20.GL_ELEMENT_ARRAY_BUFFER, 0 );

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

  // 3Dデータ実装
  protected void moveLight()
  {
    //generate_vertex_buffer();

    //フラグメントシェーダに値を渡すための変数
    varying vec3 vPositon; // 頂点→光源のベクトル
    varying vec3 vNormal; // 法線

    //材質
    //拡散反射色
    //diffuse_color

    // 鏡面反射の計算
    vec3 specular = vec3(0.0);
    if(Light > 0.0){}


  }
}