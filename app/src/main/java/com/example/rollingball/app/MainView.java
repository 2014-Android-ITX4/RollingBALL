package com.example.rollingball.app;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.EGLConfig;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;

import com.hackoeur.jglm.Vec3;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainView extends GLSurfaceView
{
  public MainRenderer renderer;
  public SceneManager scene_manager;
  public MainActivity activity;

  private float drag_base_x = 0.0f;
  private float drag_base_y = 0.0f;
  private float drag_current_x = 0.0f;
  private float drag_current_y = 0.0f;

  public MainView( Context context, MainActivity arg_activity )
  {
    super( context );

    setSystemUiVisibility
      ( View.SYSTEM_UI_FLAG_IMMERSIVE
      | View.SYSTEM_UI_FLAG_FULLSCREEN
      | View.SYSTEM_UI_FLAG_IMMERSIVE
      );

    activity = arg_activity;

    this.setEGLContextClientVersion( 2 );
    this.setEGLConfigChooser( new MainEGLConfigChooser() );
    renderer = new MainRenderer( this );
    setRenderer( renderer );

    scene_manager = new SceneManager( this );

    setOnTouchListener
      (
        new View.OnTouchListener()
        {
          @Override
          public boolean onTouch( View v, MotionEvent e )
          {
            // ドラッグとして受け付ける画面端からの距離
            final float margin = 50.0f;

            // ドラッグとして判定するべきか
            if ( e.getX() < margin || e.getX() > screen_width() - margin
              || e.getY() < margin || e.getY() > screen_height() - margin
            )
            {
              // ドラッグ処理（カメラワーク）
              ClipData data = ClipData.newPlainText( "", "" );
              v.startDrag( data, new DragShadowBuilder( v ), ( Object ) v, 0 );
              return true;
            }

            // Activity の各種タッチ系が発動（ピンチとか）
            return false;
          }
        }
      );
  }

  public Vec3 drag_delta()
  {
    float dx = drag_current_x - drag_base_x;
    float dy = drag_current_y - drag_base_y;

    drag_base_x = drag_current_x;
    drag_base_y = drag_current_y;

    return new Vec3( dx, dy, 0.0f );
  }

  @Override
  public boolean onDragEvent( DragEvent e )
  {
    boolean result = false;

    switch ( e.getAction() )
    { case DragEvent.ACTION_DRAG_ENTERED:
        Log.d("drag", "started");
        drag_base_x = e.getX();
        drag_base_y = e.getY();
        result = true;
        break;
      case DragEvent.ACTION_DRAG_LOCATION:
        Log.d("drag", "location: " + e.getX() + " " + e.getY());
        drag_current_x = e.getX();
        drag_current_y = e.getY();
        result = true;
        break;
      case DragEvent.ACTION_DROP:
        Log.d("drag", "drop");
        drag_base_x = 0.0f;
        drag_base_y = 0.0f;
        drag_current_x = 0.0f;
        drag_current_y = 0.0f;
        result = true;
        break;
      default:
        result = true;
    }

    return result;
  }

  public Bitmap load_bitmap_from_asset( String path )
  {
    Bitmap bitmap = null;

    try
    {
      bitmap = BitmapFactory.decodeStream( getResources().getAssets().open( path ) );
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }

    return bitmap;
  }

  public String load_text_from_raw_resource( int resource_id )
  {
    InputStream s = getResources().openRawResource( resource_id );
    InputStreamReader sr = new InputStreamReader( s );

    final int buffer_size = 64 * 1024;

    char[] char_buffer = new char[ buffer_size ];
    int read_size = 0;

    try
    { read_size = sr.read( char_buffer, 0, buffer_size ); }
    catch ( IOException e )
    { e.printStackTrace(); }

    return String.valueOf( char_buffer, 0, read_size );
  }

  public int screen_width()
  { return renderer.screen_width(); }

  public int screen_height()
  { return renderer.screen_height(); }
}
