package com.example.rollingball.app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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


  // データベースが作成された時に呼び出されます
  @Override
  public void onCreate( final SQLiteDatabase db )
  {
    // CONFIGテーブルを作成
    db.execSQL( "create table CONFIG " +
                  "(name text," +
                  "value real," +
                  "primary key(name))" );

    db.execSQL( "create table STAGE" +
                  "(world_id integer," +
                  "stage_id integer," +
                  "score integer" +
                  "rank integer" +
                  "primary key(world_id, stage_id)" );
  }

  @Override
  public void onUpgrade( final SQLiteDatabase sqLiteDatabase, final int i, final int i2 )
  {

  }
}
