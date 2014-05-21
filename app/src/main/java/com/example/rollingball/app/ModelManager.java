package com.example.rollingball.app;

import java.util.Map;

public class ModelManager
{

  private Map< String, ModelData > _models;

  public ModelData find( String name )
  {
    ModelData model = _models.get( name );

    if ( model == null )
      throw new NullPointerException( "model data ( name: " + name + " ) is not found." );
    
    return model;
  }

  public void add( String name , ModelData model )
  {
    _models.put( name, model );
  }

  public void remove( String name )
  {
    _models.remove( name );
  }

}