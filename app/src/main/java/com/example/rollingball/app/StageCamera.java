package com.example.rollingball.app;

import android.opengl.GLU;

import com.hackoeur.jglm.Vec3;

public class StageCamera extends Camera
{
  // プレイヤーゲームオブジェクトを基準に方位角θ、仰角φ、距離distanceを保持
  private float _distance = 10.0f; //r
  private float _theta    =  0.0f; //θ
  private float _phi      = 30.0f; //φ

  // 最小 distance 、 最大 distance
  private final float _min_distance =  1.0f;
  private final float _max_distance = 30.0f;

  // プレイヤーゲームオブジェクトを保持しておく
  private PlayerGameObject _player_game_object = null;

  // プレイヤーゲームオブジェクトをシーンから探す
  private void find_player_game_object()
  {
    _player_game_object = null;

    for ( GameObject o : scene.game_objects )
      if ( o instanceof PlayerGameObject )
        _player_game_object = (PlayerGameObject)o;
  }

  public void StageCamera( Scene scene )
  {
    // 親クラスのコンストラクターの呼び出し
    super.Camera(scene);

    // この時点でプレイヤーゲームオブジェクトがあればセットする
    find_player_game_object();
  }

  @Override
  public void update( long delta_time_in_ns )
  {
    // 複数回使うので事前に1回だけ計算して保持
    final float dst  = _distance * (float) Math.sin( _theta );

    // プレイヤーオブジェクトに対するカメラの位置差
    final Vec3 delta_position = new Vec3
      ( dst * (float) Math.cos( _phi )
      , dst * (float) Math.sin( _phi )
      , _distance * (float) Math.cos( _theta )
      );

    // プレイヤーオブジェクトが未設定の可能性があるのでテスト
    if ( _player_game_object == null )
    {
      find_player_game_object();
      // なおも見つからない場合は例外投げて死ぬ
      if ( _player_game_object == null )
        throw new NullPointerException( " this scene has not player game object." );
    }

    // カメラの視点位置をプレイヤーゲームオブジェクトを基準に軌道上の差分位置を加算して設定
    eye = _player_game_object.position.add( delta_position );

    // override 元の親クラスの update も呼んでおく
    super.update( delta_time_in_ns );
  }

  // distance を一定値範囲内だけで設定可能とするためのプロパティーセッターアクセサー
  public float distance( float value )
  { return _distance = Math.min( Math.max( value, _min_distance ), _max_distance ); }

}