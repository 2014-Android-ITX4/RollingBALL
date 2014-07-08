package com.example.rollingball.app;

public class StageScene extends Scene
{
  protected float            _stage_time_in_seconds    = 0.0f;
  protected int              _count_of_continue_ticket = 0;
  protected float            _death_height             = 0.0f;
  protected boolean          _pause                    = false;
  protected PlayerGameObject _player                   = null;
  protected FieldGameObject  _field                    = null;

  public StageScene( final SceneManager s )
  {
    super( s );

    push( _player = new PlayerGameObject( this, input_manager ) );
    this.camera = new StageCamera( this );
  }

  @Override
  public void update( final float delta_time_in_seconds )
  {
    if ( ! _pause )
      _stage_time_in_seconds += delta_time_in_seconds;

    super.update( delta_time_in_seconds );
  }

  //TODO:update()で_pauseの判定とフラグセット

}
