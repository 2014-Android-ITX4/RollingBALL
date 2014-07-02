package com.example.rollingball.app;

/**
 * Created by arakawa on 2014/06/11.
 */
import android.content.Context;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.util.Log;




public class SceneCamera extends StageCamera implements OnTouchListener{
  private GestureDetector gestureDetector;

  public SceneCamera (Context ctx)
  {
    gestureDetector = new GestureDetector( ctx, new GestureListener() );
  }

  //動くのに必要
  public boolean onTouch(View view, MotionEvent event)
  {
    Log.d( "app", "onTouch" );
    return onTouchEvent( event );
  }

  public boolean onTouchEvent(MotionEvent event) {
    Log.d("TouchEvent", "X:" + event.getX() + ",Y:" + event.getY());
    return true;
  }

  private  class GestureListener extends SimpleOnGestureListener
  //private SimpleOnGestureListener simpleOnGestureListener = new SimpleOnGestureListener()
  {
    private int swipe_distance = 100;
    private int swipe_velocity = 100;

    @Override
    public boolean onDown(MotionEvent e)
    {
      Log.d( "simple" , "Down");
      return true;
    }

    @Override
    public boolean onFling( MotionEvent e1, MotionEvent e2, float velocityX, float velocityY )
    {
      float diffX = e2.getX() - e1.getX();
      float diffY = e2.getY() - e1.getY();

      if ( Math.abs( diffX ) > Math.abs( diffY ) && Math.abs( diffX ) > swipe_distance && Math.abs( velocityX ) > swipe_velocity )
      {
        if ( diffX > 0 )
        {
          //右にスワイプ
          //θ減少
          theta( theta() - Math.abs( diffX ) );
          Log.d("simpleOn", "right");
        }
        else
        {
          //左にスワイプ
          //θ増加
          theta( theta() + Math.abs( diffX) );
          Log.d("simpleOn", "left");
        }
      }
      else if ( Math.abs( diffY ) > Math.abs( diffX ) && Math.abs( diffY ) > swipe_distance && Math.abs( velocityY ) > swipe_velocity )
      {
        if ( diffY > 0 )
        {
          //上にスワイプ
          //φ減少
          phi( phi() - Math.abs( diffY ) );
          Log.d("simpleOn", "top");
        }
        else
        {
          //下にスワイプ
          //φ増加
          phi( phi() + Math.abs( diffY ) );
          Log.d("simpleOn", "bottom");
        }
      }
      else
      {
        //45°斜めにスワイプ
        //フリック
      }
      return false;
    }
  }
}