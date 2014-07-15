package com.example.rollingball.app;

public class Test1NPC extends NPCGameObject
{
  public Test1NPC( Scene scene )
  {
    super( scene );
    this.ai( new Test1NPCAI( this ) );
  }
}
