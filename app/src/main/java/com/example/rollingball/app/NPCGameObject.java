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

}
