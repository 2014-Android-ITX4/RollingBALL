package com.example.rollingball.app;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import com.hackoeur.jglm.Vec3;

public class FieldGameObject extends GameObject
{
  public ArrayList< ArrayList< Float > > field_planes = new ArrayList< ArrayList< Float > >();
  static private float height_magnifier = 10.0f;

  public FieldGameObject( Scene scene, int arris_x, int arris_z )
  {
    super( scene );
    generate_simple_plane( arris_x, arris_z );
  }

  public FieldGameObject( Scene scene, String path )
  {
    super( scene );
    load_from_file( path );
  }

  public FieldGameObject( Scene scene )
  {
    super( scene );
    load_from_file( );
  }

  public void generate_simple_plane( int arris_x, int arris_z)
  {
    ArrayList< Float > field_line = new ArrayList< Float >();

    for ( int x = 0; x < arris_x; ++x )
    {
      for ( int z = 0; z < arris_z; ++z )
      {
        if ( x == 0 || x == arris_x - 1 )
          field_line.add( height_magnifier * 31.0f / 255.0f );
        else if (z == 0 || z == arris_z - 1 )
          field_line.add( height_magnifier * 31.0f / 255.0f );
        else
          field_line.add( 0.0f );
      }
      field_planes.add( field_line );
      field_line = new ArrayList< Float >();
    }

    model = ModelData.generate_from_field( field_planes );
  }

  // ファイル読み込み
  public void load_from_file( String path )
  {
    final float abyss_height = -1.0e+38f;

    Bitmap bitmap = _scene.scene_manager.view.load_bitmap_from_asset( path );

    ArrayList< Float > field_line = new ArrayList< Float >(  );

    float normalize_factor = height_magnifier / 255.0f;

    for ( int x = 0; x < bitmap.getWidth(); ++x )
    {
      for ( int z = 0; z < bitmap.getHeight(); ++z )
      {
        int   pixel  = bitmap.getPixel( x, z );
        float height = (float)Color.red( pixel ) * normalize_factor;

        boolean abyss = ( Color.green( pixel ) & 0x00000001 ) == 0x00000001;

        if ( abyss )
          field_line.add( abyss_height );
        else
          field_line.add( height );

//        Log.d( "FieldGameObject.load_from_file", String.valueOf( x ) + "," + z + ": " + height );
      }
      field_planes.add( field_line );
      field_line = new ArrayList< Float >(  );
    }

    model = ModelData.generate_from_field( field_planes );
  }

  // for debug, to load default test data.
  public void load_from_file()
  { load_from_file( "sample_field.png" ); }

  // GameObject基底の draw で model を描画するようにしたのでここでの Override は不要になりました。
  // see more: #123 https://github.com/2014-Android-ITX4/RollingBALL/issues/123
  //@Override
  //public void draw() { }

  public float length_x()
  { return field_planes.size(); }

  public float length_z()
  { return field_planes.get(0).size(); }

  public float y( final int x, final int z )
  { return field_planes.get( x ).get( z ); }
}