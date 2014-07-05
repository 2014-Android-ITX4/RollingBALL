package com.example.rollingball.app;

import android.opengl.GLU;
import android.util.Log;

import com.hackoeur.jglm.Vec3;
import com.hackoeur.jglm.Vec4;

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

  //端末の横幅、縦幅
  private float screen_width;
  private float screen_height;
  private float diffX;
  private float diffY;
  private float velocityX;
  private float velocityY;
  private int swipe_distance = 100;
  private int swipe_velocity = 100;
  private float pi = (float)Math.PI;
  private float omega = 0.0f;
  private float time_conversion = 1.0e-9f;
  private Vec4 event = new Vec4( 0.0f, 0.0f, 0.0f, 0.0f );

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
    screen_width = scene.scene_manager.view.screen_width();
    screen_height = scene.scene_manager.view.screen_height();

    Log.d( "simpleOn" , "StageCameraのwidth" + screen_width );
    Log.d( "simpleOn" , "StageCameraのheight" + screen_height );

    event = scene.scene_manager.view.activity.touch_event;
    diffX = event.getX();
    diffY = event.getY();
    velocityX = event.getZ();
    velocityY = event.getW();
    float asd = 0.0f;

    if ( Math.abs( diffX ) > swipe_distance && Math.abs( velocityX ) > swipe_velocity )
    {
      omega = time_conversion * pi * diffX / screen_width;
      if ( diffX > 0 )
      {
        //右にスワイプ
        //θ減少
        theta( theta() - omega * delta_time_in_ns );
        Log.d( "simpleOn", "right" );
        asd = theta();
        Log.d( "simpleOn", Float.toString( asd ) );
      }
      else
      {
        //左にスワイプ
        //θ増加
        theta( theta() + omega * delta_time_in_ns );
        Log.d( "simpleOn", "left" );
        asd = theta();
        Log.d( "simpleOn", Float.toString( asd ));
      }
    }
    if ( Math.abs( diffY ) > swipe_distance && Math.abs( velocityY ) > swipe_velocity )
    {
      omega = time_conversion * pi * diffY / screen_height;
      if ( diffY > 0 )
      {
        //上にスワイプ
        //φ減少
        phi( phi() - omega * delta_time_in_ns );
        Log.d( "simpleOn", "bottom" );
        asd = phi();
        Log.d( "simpleOn", Float.toString(asd) );
      }
      else
      {
        //下にスワイプ
        //φ増加
        phi( phi() + omega * delta_time_in_ns );
        Log.d( "simpleOn", "top" );
        asd = phi();
        Log.d( "simpleOn", Float.toString( asd ) );
      }
    }

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

