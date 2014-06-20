package com.example.rollingball.app;

import android.opengl.GLES20;
import java.util.ArrayList;
import android.util.Log;

public class FieldGameObject extends GameObject
{
  private ArrayList< ArrayList< Float > > field_planes = new ArrayList< ArrayList< Float > >();
  private int mPoint;
  private float[] mMum;
  private static String TAG = "MyGLRenderer";

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

  public void load_from_file()
  { throw new NotImplementedException(); }

  // フィールドの描画
  //頂点を三角形に
  @Override
  public void draw(ModelData model_data)
  {
    // 初期化
    GLES20.glClearColor( 0.3f, 0.3f, 0.3f, 1.0f );

    //==繰り返し処理==
    // 頂点バッファを指定する
    for ( int x = 0; model_data < x; x++ )
    {
      for ( int z = 0; z <; z++ )
      {
        GLES20.glVertexAttribPointer(
          model_data, 3, GLES20.GL_FLOAT, false, 0,
          );
        // vertices[x]のxに、indices[i]を入れる。


        // 描画
        GLES20.glDrawArrays( GLES20.GL_TRIANGLES, 0, 3 );
        checkGlError( TAG, "GLES20.glDrawArrays" );
      }
    }

  // エラーの場合
  private void checkGlError( String op )
  {
    int error;
    while ( ( error = GLES20.glGetError() ) != GLES20.GL_NO_ERROR )
    {
      Log.e( TAG, op + ": glError " + error );
      throw new RuntimeException( op + ": glError " + error );
    }
  }
}