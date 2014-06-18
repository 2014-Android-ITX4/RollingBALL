package com.example.rollingball.app;

/**
 * Created by arakawa on 2014/06/11.
 */
import android.content.Context;
import android.graphics.Point;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.util.Log;

public class SceneCamera extends StageCamera implements OnTouchListener{
  private GestureDetector gestureDetector;

  //Context con;

  /*WindowManager wm = (WindowManager)con.getSystemService(Context.WINDOW_SERVICE);
  private Display dis = wm.getDefaultDisplay();
  int width = dis.
*/

  //動くのに必要
  public boolean onTouch(View view, MotionEvent event)
  {
    return onTouchEvent( event );
  }

  public SceneCamera (Context ctx)
  {
    gestureDetector = new GestureDetector( ctx, new GestureListener() );
  }

  public boolean onTouchEvent(MotionEvent event) {
    boolean flag = gestureDetector.onTouchEvent(event);

    /*if ( event.getX() > 0 && event.getX() <  && event.getY() >= 0 )
    {
      flag = false;
    }*/
    return flag;
  }

  private  class GestureListener extends SimpleOnGestureListener
  {
    private int swipe_distance = 100;
    private int swipe_velocity = 100;

    @Override
    public boolean onFling( MotionEvent e1, MotionEvent e2, float velocityX, float velocityY )
    {
      float diffX = e2.getX() - e1.getX();
      float diffY = e2.getY() - e1.getY();

      Log.d( "MyApp", "Xの移動量" + diffX + "　Yの移動量" + diffY );

      if ( Math.abs( diffX ) > Math.abs( diffY ) && Math.abs( diffX ) > swipe_distance && Math.abs( velocityX ) > swipe_velocity )
      {
        if ( diffX > 0 )
        {
          //右にスワイプ
          //θ減少
          theta( theta() - Math.abs( diffX ) );
          return true;
        }
        else
        {
          //左にスワイプ
          //θ増加
          theta( theta() + Math.abs( diffX) );
          return true;
        }
      }
      else if ( Math.abs( diffY ) > Math.abs( diffX ) && Math.abs( diffY ) > swipe_distance && Math.abs( velocityY ) > swipe_velocity )
      {
        if ( diffY > 0 )
        {
          //上にスワイプ
          //φ減少
          phi( phi() - Math.abs( diffY ) );
          return true;
        }
        else
        {
          //下にスワイプ
          //φ増加
          phi( phi() + Math.abs( diffY ) );
          return true;
        }
      }
      else
      {
        //45°斜めにスワイプ
        //フリック
        return false;
      }
    }
  }
}