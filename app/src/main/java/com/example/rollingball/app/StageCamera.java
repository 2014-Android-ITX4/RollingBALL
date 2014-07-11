package com.example.rollingball.app;

import android.opengl.GLU;
import android.util.Log;

import com.hackoeur.jglm.Vec;
import com.hackoeur.jglm.Vec3;
import com.hackoeur.jglm.Vec4;

public class StageCamera extends Camera
{
  // プレイヤーゲームオブジェクトを基準に方位角θ、仰角φ、距離distanceを保持
  private float _distance = 20.0f; //r
  private float _theta    =  0.0f; //θ
  private float _phi      = 30.0f; //φ

  // 最小 distance 、 最大 distance
  private final float _min_distance =  1.0f;
  private final float _max_distance = 30.0f;

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
    update_swipe( delta_time_in_seconds );

    update_position();

    // override 元の親クラスの update も呼んでおく
    super.update( delta_time_in_seconds );
  }

  private  void update_position()
  {
    // 複数回使うので事前に1回だけ計算して保持
    final float dst  = _distance * (float) Math.sin( _phi );

    // プレイヤーオブジェクトに対するカメラの位置差
    final Vec3 delta_position = new Vec3
      ( dst * (float) Math.cos( _theta )
        , _distance * (float) Math.cos( _phi )
        , dst * (float) Math.sin( _theta )
      );
    //Log.d( "x","="+delta_position.getX() );
    Log.d( "y","="+delta_position.getY() );

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
    eye = look_at.add( delta_position );
  }

  private void update_swipe( float delta_time_in_seconds )
  {
    Vec3 screen_size = new Vec3
      ( scene.scene_manager.view.screen_width()
      , scene.scene_manager.view.screen_height()
      , 0.0f
      );

    //Log.d( "screen size" , screen_size.toString() );

    Vec3 dp = scene.scene_manager.view.activity.swipe_delta_position();
    Vec3 rotation_ratio = new Vec3( dp.getX() / screen_size.getX(), dp.getY() / screen_size.getY() / 4, 0.0f );

    final float rotation_magnifier = (float)Math.PI;


    if ( Math.abs( dp.getX() ) > Math.abs( dp.getY() ) )
    {
      _theta += rotation_magnifier * rotation_ratio.getX();
    }else{
      if(_phi + rotation_magnifier * rotation_ratio.getY() < 29.8f + rotation_magnifier / 2 && _phi + rotation_magnifier * rotation_ratio.getY() > 30.0f - rotation_magnifier / 2 )
      {
        _phi   += rotation_magnifier * rotation_ratio.getY();
      }
    }
    Log.d("θ, φ", "" + _theta + " " + _phi);

    scene.scene_manager.view.activity.attenuate_swipe_state();
    //scene.scene_manager.view.activity.reset_swipe_state();
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

