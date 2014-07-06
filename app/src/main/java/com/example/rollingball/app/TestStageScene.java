package com.example.rollingball.app;

public class TestStageScene extends StageScene
{

  TestStageScene( final SceneManager s )
  {
    super( s );

    push( _field = new FieldGameObject( 10, 10 ) );
  }

  @Override
  public void update( float delta_time_in_seconds )
  {

  }
}
