package com.example.rollingball.app;

/**
 * Created by arakawa on 2014/06/11.
 */
import android.util.Log;

import com.hackoeur.jglm.Vec4;


public class SceneCamera extends StageCamera{
  private float diffX;
  private float diffY;
  private float velocityX;
  private float velocityY;

  private int swipe_distance = 100;
  private int swipe_velocity = 100;
  private int screenX;
  private int screenY;
  private float pi = 3.14f;
  private Vec4 event = new Vec4( 0.0f, 0.0f, 0.0f, 0.0f );

  public SceneCamera()
  {
    event = scene.scene_manager.view.activity.touch_event;

    //screenX = scene.scene_manager.view.screen_size_X;
    //screenY = scene.scene_manager.view.screen_size_Y;

    diffY = event.getY();
    diffX = event.getX();
    velocityX = event.getZ();
    velocityY = event.getW();
    float asd = 0.0f;
    Log.d( "simpleOn", "横:" + screenX );
    Log.d( "simpleOn", "縦:" + screenY );

    if ( Math.abs( diffX ) > Math.abs( diffY ) && Math.abs( diffX ) > swipe_distance && Math.abs( velocityX ) > swipe_velocity )
    {
      if ( diffX > 0 )
      {
        //右にスワイプ
        //θ減少
        theta( theta() - Math.abs( diffX / screenX ) * pi );
        Log.d( "simpleOn", "right" );
        asd = theta();
        Log.d( "simpleOn", Float.toString( asd ) );
      }
      else
      {
        //左にスワイプ
        //θ増加
        theta( theta() + Math.abs( diffX / screenX ) * pi );
        Log.d( "simpleOn", "left" );
        asd = theta();
        Log.d( "simpleOn", Float.toString( asd ));
      }
    }
    else if ( Math.abs( diffY ) > Math.abs( diffX ) && Math.abs( diffY ) > swipe_distance && Math.abs( velocityY ) > swipe_velocity )
    {
      if ( diffY > 0 )
      {
        //下にスワイプ
        //φ減少
        phi( phi() - Math.abs( diffY / screenY ) * pi );
        Log.d( "simpleOn", "bottom" );
        asd = phi();
        Log.d( "simpleOn", Float.toString(asd) );
      }
      else
      {
        //上にスワイプ
        //φ増加
        phi( phi() + Math.abs( diffY / screenY ) * pi );
        Log.d( "simpleOn", "top" );
        asd = phi();
        Log.d( "simpleOn", Float.toString( asd ) );
      }
    }
    else
    {
      //45°斜めにスワイプ
      //フリック
    }
  }
}
