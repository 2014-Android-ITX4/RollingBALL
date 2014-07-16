package com.example.rollingball.app;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;

import com.hackoeur.jglm.Vec3;
import com.hackoeur.jglm.Vec4;

import java.util.List;


public class MainActivity
  extends ActionBarActivity implements SensorEventListener
{
  public SensorManager sensor_manager;

  public Vec3 rotation = new Vec3( 0.0f, 0.0f, 0.0f );

  private MainView _view;
  private boolean _is_magnetic_sensor, _is_accelerometer_sensor;

  private GestureDetector _gestureDetector;

  private Vec3 _swipe_delta_position = new Vec3( 0.0f, 0.0f, 0.0f );
  private Vec3 _swipe_velocity = new Vec3( 0.0f, 0.0f, 0.0f );

  @Override
  protected void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    _view = new MainView( this, this );

    setContentView( _view );

    //センサ・マネージャの取得
    sensor_manager = ( SensorManager )getSystemService( SENSOR_SERVICE );

    _gestureDetector = new GestureDetector( this, onGestureListener );

  }

  @Override
  protected void onResume()
  {
    super.onResume();

    //センサの取得
    List< Sensor > sensors = sensor_manager.getSensorList( Sensor.TYPE_ALL );

    //センサーマネージャーへリスナー登録
    for ( Sensor sensor : sensors )
    {
      if ( sensor.getType() == Sensor.TYPE_ACCELEROMETER )
      {
        sensor_manager.registerListener( this, sensor, SensorManager.SENSOR_DELAY_GAME );
        _is_accelerometer_sensor = true;
      }
    }

//    _view = new MainView( this, this );

    _view.onResume();
  }

  @Override
  protected void onPause()
  {
    super.onPause();

    //センサーマネージャーのリスナー登録破棄
    if ( _is_accelerometer_sensor )
    {
      sensor_manager.unregisterListener( this );
      _is_accelerometer_sensor = false;
    }

    _view.onPause();
  }

  // センサーの精度が変更された時に呼び出されるメソッド
  @Override
  public void onAccuracyChanged( final Sensor sensor, final int i )
  {
    //現状は使いません
  }

  // センサーの値が変更された時に呼び出されるメソッド
  @Override
  public void onSensorChanged( final SensorEvent event )
  {
    if ( event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE )
      return;

    if ( event.sensor.getType() == Sensor.TYPE_ACCELEROMETER )
      rotation = calc_orientation_from_accelerometer( new Vec3( event.values[0],event.values[1],event.values[2] ) );
  }

  Vec3 calc_orientation_from_accelerometer( Vec3 acceleration )
  {
    return new Vec3
      ( calc_axis_rotation_from_acceleration( acceleration.getY(), acceleration.getZ() )
      , 0.0f
      , calc_axis_rotation_from_acceleration( acceleration.getX(), acceleration.getZ() )
      );
  }

  float calc_axis_rotation_from_acceleration( float t1, float t2 )
  {
    final float pih = (float)( Math.PI * 0.5 );

    // t2+
    if ( t2 >= 0.0f )
      return pih * t1 * 1.0e-1f;
      // t2- and t1-
    else if ( t1 < 0.0f )
      return pih * ( -1.0e+1f - t1 ) * 1.0e-1f - pih;
      // t2- and t1+
    else
      return pih * ( 1.0e+1f - t1 ) * 1.0e-1f + pih;
  }

  int radianToDegree(float rad)
  {
    return (int) Math.floor( Math.toDegrees(rad) ) ;
  }

  @Override
  public boolean onCreateOptionsMenu( Menu menu )
  {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate( R.menu.main, menu );
    return true;
  }

  @Override
  public boolean onOptionsItemSelected( MenuItem item )
  {
    int id = item.getItemId();
    if ( id == R.id.action_settings )
    {
      return true;
    }
    return super.onOptionsItemSelected( item );
  }

  @Override
  public boolean onTouchEvent( MotionEvent event ) {
    _gestureDetector.onTouchEvent( event );
    return false;
  }

  public Vec3 swipe_delta_position()
  { return _swipe_delta_position; }

  public Vec3 swipe_velocity()
  { return _swipe_velocity; }

  public void attenuate_swipe_state()
  {
    final float attenuation = 0.90f;

    if ( _swipe_delta_position.getLengthSquared() > 0.05f )
      _swipe_delta_position = _swipe_delta_position.multiply( attenuation );
    else
      _swipe_delta_position = Vec3.VEC3_ZERO;

    if ( _swipe_velocity.getLengthSquared() > 0.05f )
      _swipe_velocity = _swipe_velocity.multiply( attenuation );
    else
      _swipe_velocity = Vec3.VEC3_ZERO;
  }

  public void reset_swipe_state()
  {
    _swipe_delta_position = Vec3.VEC3_ZERO;
    _swipe_velocity       = Vec3.VEC3_ZERO;
  }

  private GestureDetector.SimpleOnGestureListener onGestureListener = new GestureDetector.SimpleOnGestureListener()
  {
    @Override
    public boolean onFling(MotionEvent motion_event_1, MotionEvent motion_event_2, float velocity_x, float velocity_y )
    {
      _swipe_delta_position = new Vec3( motion_event_2.getX() - motion_event_1.getX(), motion_event_2.getY() - motion_event_1.getY(), 0.0f );
      _swipe_velocity = new Vec3( velocity_x, velocity_y, 0.0f );

      Log.d( "swipe delta position", _swipe_delta_position.toString() );
      Log.d( "swipe velocity", _swipe_velocity.toString() );

      return false;
    }
  };

}