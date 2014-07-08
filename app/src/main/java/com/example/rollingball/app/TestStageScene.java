package com.example.rollingball.app;

public class TestStageScene extends StageScene
{

  public TestStageScene( final SceneManager s )
  {
    super( s );

    _player.model = ModelData.generate_sphere();
    //game_objects.clear();

    push( _field = new FieldGameObject( this ) );
  }

  @Override
  public void update( float delta_time_in_seconds )
  {
    super.update( delta_time_in_seconds );
  }
}
