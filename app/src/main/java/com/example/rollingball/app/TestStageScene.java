package com.example.rollingball.app;

public class TestStageScene extends StageScene
{

  TestStageScene( final SceneManager s )
  {
    super( s );

    _player.model = ModelData.generate_sphere();

    //push( _field = new FieldGameObject( 10, 10 ) );
  }

  @Override
  public void update( float delta_time_in_seconds )
  {
    super.update( delta_time_in_seconds );
  }
}
