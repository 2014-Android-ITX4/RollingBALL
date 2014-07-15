package com.example.rollingball.app;

public class Test1NPC extends NPCGameObject
{
  public Test1NPC( Scene scene )
  {
    super( scene );

    this.mass = 0.1f;
    this.collision_radiuses.add( 0.5f );

    this.ai( new Test1NPCAI( this ) );
  }
}
