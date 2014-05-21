package com.example.rollingball.app;

/**
 * Created by sakamoto on 2014/05/21.
 */
public class StageScene extends Scene
{
  protected float _stage_time;
  protected int _count_of_continue_ticket = 0;
  protected float _death_height;
  protected boolean _pause;

  StageScene( final SceneManager s )
  {
    super( s );
  }

  @Override
  public void update( final long delta_time_in_ns )
  {
    if ( _pause ) _stage_time++;
    super.update( delta_time_in_ns );
  }

  //TODO:update()で_pauseの判定とフラグセット

}
