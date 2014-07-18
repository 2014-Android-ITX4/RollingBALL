package com.example.rollingball.app;

import android.util.Log;

import com.hackoeur.jglm.Vec3;

public class TestStageScene extends StageScene
{

  public TestStageScene( final SceneManager s )
  {
    super( s );

    field( new FieldGameObject( this, 16, 16 ) );

    _player.position = new Vec3( _field.length_x() * 0.5f, 10.0f, _field.length_z() * 0.5f );
    _player.model = ModelData.generate_sphere();
    _player.model.material.diffuse_color( new Vec3( 0.6f, 0.6f, 1.0f ) );
    //game_objects.clear();

    // テスト用NPCを配置
    random_npc( 3 );

    this.lighting.position( new Vec3( _player.position.getX(), _player.position.getY() + 25.0f, _player.position.getZ() ) );
  }

  @Override
  public void update( float delta_time_in_seconds )
  {
    super.update( delta_time_in_seconds );
    lighting.position( _player.position.add( new Vec3( 0.0f, 5.0f, 0.0f ) ) );
  }

  private void random_npc( int num_of_npcs )
  {
    for ( int n = 0; n < num_of_npcs; ++n )
    {
      Test1NPC npc = new Test1NPC( this );
      npc.position = new Vec3
        ( (float)Math.random() * _field.length_x()
        , ( (float)Math.random() - 0.5f ) * _player.position.getY() + _player.position.getY()
        , (float)Math.random() * _field.length_z()
        );
      npc.model = ModelData.generate_sphere();
      npc.model.material.diffuse_color( new Vec3( 0.6f, 1.0f, 0.6f ) );
      push( npc );
      //Log.d( "", npc.position.toString() );
    }
  }

  private void field( final FieldGameObject f )
  {
    push( _field = f );
  }
}
