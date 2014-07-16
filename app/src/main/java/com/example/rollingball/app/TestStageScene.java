package com.example.rollingball.app;

import com.hackoeur.jglm.Vec3;

public class TestStageScene extends StageScene
{

  public TestStageScene( final SceneManager s )
  {
    super( s );

    _player.model = ModelData.generate_sphere();
    //game_objects.clear();

    push( _field = new FieldGameObject( this, "test_1_64x64.png" ) );

    this.lighting.position( new Vec3( _player.position.getX(), _player.position.getY() + 25.0f, _player.position.getZ() ) );
  }

  @Override
  public void update( float delta_time_in_seconds )
  {
    super.update( delta_time_in_seconds );
  }
}
