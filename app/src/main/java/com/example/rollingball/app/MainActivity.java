package com.example.rollingball.app;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.hackoeur.jglm.Vec3;

import java.util.List;


public class MainActivity
  extends ActionBarActivity implements SensorEventListener
{

  private MainView _view;
  public SensorManager sensor_manager;
  public Vec3 orientation;
  private boolean _is_magnetic_sensor, _is_accelerometer_sensor;
  private static final int MATRIX_SIZE = 16;

  // 回転行列
  private float[] _in_r = new float[ MATRIX_SIZE ];
  private float[] _out_r = new float[ MATRIX_SIZE ];
  private float[] _i = new float[ MATRIX_SIZE ];

  // センサーの値
  public float[] orientation_values = new float[ 3 ];
  private float[] _magnetic_values = new float[ 3 ];
  private float[] _accelerometer_values = new float[ 3 ];


  @Override
  protected void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.activity_main );

    //センサ・マネージャの取得
    sensor_manager = ( SensorManager )getSystemService( SENSOR_SERVICE );

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
      if ( sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD )
      {
        sensor_manager.registerListener( this, sensor, SensorManager.SENSOR_DELAY_GAME );
        _is_magnetic_sensor = true;
      }

      if ( sensor.getType() == Sensor.TYPE_ACCELEROMETER )
      {
        sensor_manager.registerListener( this, sensor, SensorManager.SENSOR_DELAY_GAME );
        _is_accelerometer_sensor = true;
      }

    }
  }

  @Override
  protected void onPause()
  {
    super.onPause();

    //センサーマネージャーのリスナー登録破棄
    if ( _is_magnetic_sensor || _is_accelerometer_sensor )
    {
      sensor_manager.unregisterListener( this );
      _is_magnetic_sensor = false;
      _is_accelerometer_sensor = false;
    }
  }

  // センサーの精度が変更された時に呼び出されるメソッド
  @Override
  public void onAccuracyChanged( final Sensor sensor, final int i )
  {
    //現状Stub
  }

  // センサーの値が変更された時に呼び出されるメソッド
  @Override
  public void onSensorChanged( final SensorEvent event )
  {
    if ( event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE )
      return;

    switch ( event.sensor.getType() )
    {
      case Sensor.TYPE_MAGNETIC_FIELD:
        _magnetic_values = event.values.clone();
        break;

      case Sensor.TYPE_ACCELEROMETER:
        _accelerometer_values = event.values.clone();
        break;
    }

    if ( _magnetic_values != null && _accelerometer_values != null )
    {
      SensorManager.getRotationMatrix( _in_r, _i, _accelerometer_values, _magnetic_values );

      // Activity表示が縦の場合。横になるか縦になるか決まったら修正の必要あり。
      SensorManager.remapCoordinateSystem( _in_r, SensorManager.AXIS_X, SensorManager.AXIS_Z,
                                           _out_r
      );
      SensorManager.getOrientation( _out_r, orientation_values );

    }
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
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();
    if ( id == R.id.action_settings )
    {
      return true;
    }
    return super.onOptionsItemSelected( item );
  }
}
