package com.example.rollingball.app;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import android.content.res.AssetManager;

public class FieldGameObject extends GameObject
{
  private ArrayList< ArrayList< Float > > field_planes = new ArrayList< ArrayList< Float > >();

  public void generate_simple_plane( int arris_x, int arris_z)
  {
    ArrayList< Float > field_lines = new ArrayList< Float >();

    for ( int x = 0 ; x < arris_x ; x++ )
    {
      for ( int z = 0 ; z < arris_z ; z++ )
      {
        if ( x == 0 || x == arris_x - 1 )
          field_lines.add( 1.0f );
        else if (z == 0 || z == arris_z - 1 )
          field_lines.add( 1.0f );
        else
          field_lines.add( 0.0f );
      }
      field_planes.add( field_lines );
      field_lines = new ArrayList< Float >();
    }

  }

  // ファイル読み込み
  public void load_from_file()
  {
    try {
      //Resources res = getResources();
      AssetManager as = getResources().getAssets();
      InputStream in = as.open( "sample_field.png" );
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}