package com.example.rollingball.app;

public class ItemGameObject extends GameObject
{
  private final Item _item;

  public ItemGameObject( Scene scene, final Item item )
  {
    super( scene );
    _item = item;
  }
}