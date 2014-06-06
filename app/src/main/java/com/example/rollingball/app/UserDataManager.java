package com.example.rollingball.app;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class UserDataManager
{


  UserDataManager( Context context )
  {

  }

  public void save_config( String name, float value)
  {
    // TODO:引数を元にDBのコンフィグの値を更新
    String sql;

    sql = "update CONFIG set value = " + String.valueOf( value ) +
          ", where name = '" + name + "'";


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
    try
    {

      //TODO:ここで初期データをすべて挿入する

    } catch ( SQLException e )
    {
      Log.e( "SQL ERROR", e.toString() );
    }
  }



}
