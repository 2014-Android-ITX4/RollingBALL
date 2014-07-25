package com.example.rollingball.app;

import com.hackoeur.jglm.Vec3;

public class StageCamera extends Camera
{
  // プレイヤーゲームオブジェクトを基準に方位角θ、仰角φ、距離distanceを保持
  private float _distance = 10.0f; //r
  private float _theta    =  -(float)Math.PI * 3.0f / 4.0f; //θ
  private float _phi      =  +(float)Math.PI / 3.0f; //φ

  private Vec3 _delta_position;

  private Vec3 _screen_size;
  private Vec3 _delta;
  private Vec3 _rotation_ratio;

  // 最小 distance 、 最大 distance
  private final float _min_distance =  5.0f;
  private final float _max_distance = 30.0f;
  private float _update_swipe_wait = 0.0f;
  // プレイヤーゲームオブジェクトを保持しておく
  private PlayerGameObject _player_game_object = null;

  public StageCamera( final Scene scene )
  {
    super( scene );

    // この時点でプレイヤーゲームオブジェクトがあればセットする
    find_player_game_object();
  }

  // プレイヤーゲームオブジェクトをシーンから探す
  private void find_player_game_object()
  {
    _player_game_object = null;

    for ( GameObject o : scene.game_objects )
      if ( o instanceof PlayerGameObject )
        _player_game_object = (PlayerGameObject)o;
  }

  @Override
  public void update( float delta_time_in_seconds )
  {
    distance( distance() - scene.input_manager.result_scale );

    // # 235 ピンチでカメラ距離を操作した直後にスワイプ判定が意図せず動作する問題の対応
    if ( scene.input_manager.result_scale != 0.0f )
      _update_swipe_wait = 0.3f;
    else if ( _update_swipe_wait > 0.0f )
      _update_swipe_wait -= delta_time_in_seconds;
    else
      update_camera( delta_time_in_seconds );

    update_position();

    // override 元の親クラスの update も呼んでおく
    super.update( delta_time_in_seconds );
  }

  private  void update_position()
  {
    // プレイヤーオブジェクトに対するカメラの位置差
    _delta_position = new Vec3
      ( (float)Math.sin( _theta ) * (float)Math.cos( _phi )
      , (float)Math.sin( _phi )
      , (float)Math.cos( _theta ) * (float)Math.cos( _phi )
      ).multiply( _distance );
    //Log.d( "x=","x="+delta_position.getX() );
    //Log.d( "y=","y="+delta_position.getY() );

//    Log.d("θ, φ", "" + _theta + " " + _phi + " " + delta_position.toString() );

    // プレイヤーオブジェクトが未設定の可能性があるのでテスト
    if ( _player_game_object == null )
    {
      find_player_game_object();
      // なおも見つからない場合は例外投げて死ぬ
      if ( _player_game_object == null )
        throw new NullPointerException( " this scene has not player game object." );
    }

    // プレイヤー方向を向く
    look_at = _player_game_object.position;

    // カメラの視点位置をプレイヤーゲームオブジェクトを基準に軌道上の差分位置を加算して設定
    eye = look_at.add( _delta_position );
  }

  private Vec3 camera_velocity = Vec3.VEC3_ZERO;

  private void update_camera( float delta_time_in_seconds )
  {
    _screen_size = new Vec3
      ( scene.scene_manager.view.screen_width()
      , scene.scene_manager.view.screen_height()
      , 0.0f
      );

    //Log.d( "screen size" , screen_size.toString() );

    _delta = scene.scene_manager.view.drag_delta();
    if ( Helper.xy_length( _delta ) > 0.0f )
      camera_velocity = _delta;
    else
      camera_velocity = camera_velocity.multiply( 0.95f );

    _rotation_ratio = new Vec3( camera_velocity.getX() / _screen_size.getX(), camera_velocity.getY() / _screen_size.getY(), 0.0f );

    if ( Math.abs( camera_velocity.getX() ) > Math.abs( camera_velocity.getY() ) )
    {
      final float rotation_magnifier = (float)Math.PI / 4.0f;
      _theta += rotation_magnifier * _rotation_ratio.getX();
    }
    else
    {
      final float rotation_magnifier = (float)Math.PI / 8.0f;
      _phi += rotation_magnifier * _rotation_ratio.getY();

      // ジンバルロックを回避とカメラのUP反転の対応として仰角を制限します。
      final float phi_min = -(float)Math.PI * 0.5f * 0.98f;
      final float phi_max = +(float)Math.PI * 0.5f * 0.98f;
      _phi = Math.min( Math.max( _phi, phi_min ), phi_max );
    }

    //camera_velocity.multiply( 0.3f );

    //Log.d("θ, φ", "" + _theta + " " + _phi);
  }

  // distance を一定値範囲内だけで設定可能とするためのプロパティーセッターアクセサー
  public float distance( float value )
  { return _distance = Math.min( Math.max( value, _min_distance ), _max_distance ); }

  public float distance()
  { return _distance; }

  public float theta( float value )
  { return _theta = value; }

  public float theta()
  { return _theta; }

  public float phi( float value )
  { return _phi = value; }

  public float phi()
  { return _phi; }
}
