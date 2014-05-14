package com.example.rollingball.app;

import com.hackoeur.jglm.Mat4;

/**
 * Created by Tukasa on 2014/05/14.
 */
public class ModelData
{
  private int _vertex_array_object_id;
  private int _vertices_buffer_id;
  private int _indices_buffer_id;
  private int _texture_buffer_id;

  public static ModelData generate_sphere( double radius )
  {
    return null;
  }

  public static ModelData generate_cube( double arris_length )
  {
    return null;
  }

  public static ModelData load_from_file( String file_path )
  {
    return null;
  }

  public void draw( Mat4 trasformation )
  {

  }

}
