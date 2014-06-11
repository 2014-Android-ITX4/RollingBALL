package com.example.rollingball.app;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class UserDataManager
{
  SaveDataSQLiteOpenHelper helper;
  SQLiteDatabase db;

  private SceneManager manager;

  public UserDataManager(SceneManager arg_manager)
  {
    manager = arg_manager;

    helper = new SaveDataSQLiteOpenHelper( manager.view.activity );
    db = helper.getWritableDatabase();
  }

  // CONFIGの設定情報を更新します
  public void save_config( String name, float value)
  {
    String sql;

    sql = "update CONFIG set value = " + String.valueOf( value ) +
          ", where name = '" + name + "'";
    db.execSQL( sql );


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
