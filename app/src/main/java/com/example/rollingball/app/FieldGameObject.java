package com.example.rollingball.app;

import java.util.ArrayList;

public class FieldGameObject extends GameObject
{
  private ArrayList< ArrayList< Float > > field_planes = new ArrayList< ArrayList< Float > >();

  public void generate_simple_plane( int arris_x, int arris_z)
  {
    ArrayList< Float > field_lines = new ArrayList< Float >();
    for ( int x = 0 ; x < arris_x ; x++ ){
      for ( int z = 0 ; z < arris_z ; z++ ){
        if ( x == 0 || x == arris_x - 1 ) field_lines.add( 1.0f );
        else if (z == 0 || z == arris_z - 1 ) field_lines.add( 1.0f );
        else field_lines.add( 0.0f );
      }
      field_planes.add( field_lines );
      field_lines = new ArrayList< Float >();
    }

  }
  public void load_from_file(){ throw new NotImplementedException(); }
}