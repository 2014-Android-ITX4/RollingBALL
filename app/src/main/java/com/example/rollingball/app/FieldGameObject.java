package com.example.rollingball.app;

import android.opengl.GLES20;
import java.util.ArrayList;
import
import android.app.Activity;
import android.content.pm.FeatureInfo;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.opengl.GLES10;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.ViewTreeObserver;
import java.lang.reflect.Field;

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
  //頂点を三角形に
  @Override
  public void draw(ModelData model_data)
  {

    //三角形の頂点座標(ModelDataから得る)

    // 三角形の描画
    ModelData model_d = model_data;

    //==ループして処理==
    for ( int x = 0; x < model_d; x++ )
    {
      for ( int z = 0; z < model_d; z++ )
      {
        GLES20.glDrawArrays(model_data);
        checkGlError( "glDrawArrays" );
      }
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