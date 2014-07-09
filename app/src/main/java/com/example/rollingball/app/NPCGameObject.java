package com.example.rollingball.app;

public class NPCGameObject extends LifeGameObject
{

  private NPCAI _ai = new NPCAI( this );

  public NPCGameObject( final Scene scene )
  {
    super( scene );
  }

  public void ai( final NPCAI ai )
  {
    _ai = ai;
  }

  @Override
  public void update( final float delta_time_in_seconds )
  {
    _ai.update( delta_time_in_seconds );

    super.update( delta_time_in_seconds );
  }
}
