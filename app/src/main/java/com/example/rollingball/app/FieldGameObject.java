package com.example.rollingball.app;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

public class FieldGameObject extends GameObject
{
  private ArrayList< ArrayList< Float > > field_planes = new ArrayList< ArrayList< Float > >();

  public void generate_simple_plane( int arris_x, int arris_z)
  {
    ArrayList< Float > field_line = new ArrayList< Float >();

    for ( int x = 0; x < arris_x; ++x )
    {
      for ( int z = 0; z < arris_z; ++z )
      {
        if ( x == 0 || x == arris_x - 1 )
          field_line.add( 1.0f );
        else if (z == 0 || z == arris_z - 1 )
          field_line.add( 1.0f );
        else
          field_line.add( 0.0f );
      }
      field_planes.add( field_line );
      field_line = new ArrayList< Float >();
    }
  }

  // ファイル読み込み
  public void load_from_file()
  {
    Bitmap bitmap = _scene.scene_manager.view.load_bitmap_from_asset( "sample_field.png" );

    ArrayList< Float > field_line = new ArrayList< Float >(  );

    float normalize_factor = 1.0f / 255.0f;

    for ( int x = 0; x < bitmap.getWidth(); ++x )
    {
      for ( int z = 0; z < bitmap.getHeight(); ++z )
      {
        int   pixel  = bitmap.getPixel( x, z );
        float height = (float)Color.red( pixel ) * normalize_factor;
        field_line.add( height );
        Log.d("FieldGameObject.load_from_file", String.valueOf( x ) + "," + z + ": " + height );
      }
      field_planes.add( field_line );
      field_line = new ArrayList< Float >(  );
    }
  }
}