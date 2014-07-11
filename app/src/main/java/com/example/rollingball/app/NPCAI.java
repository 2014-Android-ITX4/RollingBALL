package com.example.rollingball.app;

public class NPCAI implements IUpdatable
{

  private LifeGameObject _life_game_object = null;

  public NPCAI( final LifeGameObject control_target )
  { _life_game_object = control_target; }

  @Override
  public void update( final float delta_time_in_seconds )
  { }
}
