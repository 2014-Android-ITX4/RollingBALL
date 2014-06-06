package com.example.rollingball.app;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class UserDataManager extends SQLiteOpenHelper
{

  UserDataManager( Context context )
  {
    // DBを作成します
    super( context, "SAVEDATA",  null, 1 );
  }

  public void save_config( String name, float value)
  {
    // TODO:引数を元にDBのコンフィグの値を更新
  }

  public void save_stage( int world_id, int stage_id, int score, int rank )
  {
    // TODO:引数を元にステージのスコア・ランクの値を更新
  }

  public void save_item( int item_id, int number )
  {
    // TODO:引数を元にアイテムの値を更新
  }

  public void load(){}

  // 初期データを挿入します。
  private void insert_newdata()
  {

  }


  // データベースが作成された時に呼び出されます
  @Override
  public void onCreate( final SQLiteDatabase db )
  {

    try
    {
      // CONFIGテーブルを作成
      db.execSQL( "create table CONFIG" +
                    "(name text not null," +
                    "value real not null," +
                    "primary key(name))" );

      // STAGEテーブルを作成
      db.execSQL( "create table STAGE" +
                    "(world_id integer not null," +
                    "stage_id integer not null," +
                    "score integer, not null" +
                    "rank integer, not null" +
                    "primary key(world_id, stage_id)" );

      // ITEMテーブルを作成
      db.execSQL( "create table ITEM" +
                    "(id integer not null," +
                    "number integer not null," +
                    "primary key(id))" );

    } catch ( SQLException e )
    {
      Log.e( "SQL ERROR", e.toString() );
    }



  }

  @Override
  public void onUpgrade( final SQLiteDatabase sqLiteDatabase, final int i, final int i2 )
  {

  }
}
