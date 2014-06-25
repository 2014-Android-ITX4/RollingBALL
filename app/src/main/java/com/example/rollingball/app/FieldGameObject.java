package com.example.rollingball.app;

import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ByteBuffer;
import android.opengl.GLES20;
import java.util.ArrayList;
import android.util.Log;

public class FieldGameObject extends GameObject
{
  private ArrayList< ArrayList< Float > > field_planes = new ArrayList< ArrayList< Float > >();
  private FloatBuffer mVertexBuffer;  // 頂点バッファ
  private ByteBuffer mIndexBuffer;    // インデックスバッファ
  public static int positionHandle;   //位置ハンドル
  public static int colorHandle;     //色ハンドル
  private static int program;//プログラムオブジェクト

  public void generate_simple_plane( int arris_x, int arris_z )
  {
    ArrayList< Float > field_lines = new ArrayList< Float >();

    for ( int x = 0; x < arris_x; x++ )
    {
      for ( int z = 0; z < arris_z; z++ )
      {
        if ( x == 0 || x == arris_x - 1 ) field_lines.add( 1.0f );
        else if ( z == 0 || z == arris_z - 1 ) field_lines.add( 1.0f );
        else field_lines.add( 0.0f );
      }
      field_planes.add( field_lines );
      field_lines = new ArrayList< Float >();
    }
  }

  private void FieldGameObject( double field_length )
  {
    // TODO field__lengthの値を反映させる
    float[] vertices = {
      1.0f, 1.0f, 1.0f,//頂点0
      1.0f, 1.0f, -1.0f,//頂点1
      -1.0f, 1.0f, 1.0f,//頂点2
      -1.0f, 1.0f, -1.0f,//頂点3
      1.0f, -1.0f, 1.0f,//頂点4
      1.0f, -1.0f, -1.0f,//頂点5
      -1.0f, -1.0f, 1.0f,//頂点6
      -1.0f, -1.0f, -1.0f,//頂点7
    };
    mVertexBuffer = makeFloatBuffer( vertices );

    //インデックスバッファの生成
    byte[] indices = {
      0, 1, 2, 3, 6, 7, 4, 5, 0, 1,//面0
      1, 5, 3, 7,         //面1
      0, 2, 4, 6,        //面2
    };
    mIndexBuffer = makeByteBuffer( indices );
  }

  // シェーダー
  public class initShader
  {
    //頂点シェーダのコード
    private final static String VERTEX_CODE = "attribute vec4 a_Position;" +
      "void main(){" +
      "gl_Position=a_Position;" +
      "}";

    //フラグメントシェーダのコード
    private final static String FRAGMENT_CODE = "precision mediump float;" +
      "uniform vec4 u_Color;" +
      "void main(){" +
      "gl_FragColor=u_Color;" +
      "}";

  }

    //プログラムの生成
  public static void makeProgram() {
      //シェーダーオブジェクトの生成
      int vertexShader=loadShader(GLES20.GL_VERTEX_SHADER,VERTEX_CODE);
      int fragmentShader=loadShader(GLES20.GL_FRAGMENT_SHADER,FRAGMENT_CODE);

      //プログラムオブジェクトの生成
      program=GLES20.glCreateProgram();
      GLES20.glAttachShader(program,vertexShader);
      GLES20.glAttachShader(program,fragmentShader);
      GLES20.glLinkProgram(program);

      //ハンドルの取得
      positionHandle=GLES20.glGetAttribLocation(program,"a_Position");
      colorHandle=GLES20.glGetUniformLocation(program,"u_Color");

      //プログラムオブジェクトの利用開始
      GLES20.glUseProgram(program);
    }

    //シェーダーオブジェクトの生成
    private static int loadShader(int type,String shaderCode) {
      int shader=GLES20.glCreateShader(type);
      GLES20.glShaderSource(shader,shaderCode);
      GLES20.glCompileShader(shader);
      return shader;
    }

  public void load_from_file()
  { throw new NotImplementedException(); }

  // フィールド描画処理
  @Override
  public void draw( GLES20 gles20 )
  {
    //画面のクリア
    GLES20.glClearColor( 0.5f, 0.5f, 0.5f, 0.5f );

     GLES20.glUseProgram(program);

    // 頂点バッファを指定する
    GLES20.glVertexAttribPointer(,3,GLES20.GL_FLOAT,false,0,mVertexBuffer);

    //色の指定
    GLES20.glUniform4f(this.colorHandle,1.0f,0.0f,0.0f,1.0f);

    // 描画
    GLES20.glDrawElements( GLES20.GL_TRIANGLES, GLES20.GL_UNSIGNED_BYTE, 6, mIndexBuffer );
  }

  //byte配列をByteBufferに変換
  private ByteBuffer makeByteBuffer( byte[] array )
  {
    ByteBuffer bb = ByteBuffer.allocateDirect( array.length ).order(
      ByteOrder.nativeOrder()
    );
    bb.put( array ).position( 0 );
    return bb;
  }

  //float配列をFloatBufferに変換
  private FloatBuffer makeFloatBuffer( float[] array )
  {
    FloatBuffer fb = ByteBuffer.allocateDirect( array.length * 3 ).order(
      ByteOrder.nativeOrder()
    ).asFloatBuffer();
    fb.put( array ).position( 0 );
    return fb;
  }
}
