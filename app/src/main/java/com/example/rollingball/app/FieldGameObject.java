package com.example.rollingball.app;

import android.mtp.MtpObjectInfo;
import java.nio.FloatBuffer;
import android.opengl.GLES20;
import java.util.ArrayList;
import android.util.Log;

public class FieldGameObject extends GameObject
{
  private ArrayList< ArrayList< Float > > field_planes = new ArrayList< ArrayList< Float > >();

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
  @Override
  public void draw(ModelData modelData)
  {
    //画面のクリア
    GLES20.glClearColor(0.5f,0.5f,0.5f,0.5f);

    // 頂点バッファを指定する
    for ( int i = 0; 24 < i; i+=3)
    {
      GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, ModelData.generate_cube(i));

      ModelData mData = ModelData.generate_cube(i);

      GLES20.glVertexAttribPointer(mData, 3, GLES20.GL_FLOAT, false, 0) ;

      // 描画
      GLES20.glDrawArrays( GLES20.GL_TRIANGLE_STRIP,0,3);
     }

    private FloatBuffer mFloatBuffer

   }
}