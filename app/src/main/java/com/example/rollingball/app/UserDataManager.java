package com.example.rollingball.app;

import android.content.ContentValues;
import android.database.Cursor;
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

    // CONFIGテーブルが空 = データ入ってなければ初期データ挿入
    Cursor cursor = db.rawQuery( "select * from CONFIG", null );
    cursor.moveToFirst();

    if ( cursor.getCount() == 0 )
      insert_newdata();
  }

  // CONFIGの設定情報を更新します
  public void save_config( String name, float value)
  {
    ContentValues values = new ContentValues();
    values.put( "value", value );

    try
    {
      db.update( "CONFIG", values, "name = ?", new String[]{ name } );
    } catch ( SQLException e )
    {
      Log.e( "save_config SQL ERROR", e.toString() );
    }
 }

  // 引数のステージデータをセーブします
  public void save_stage( int world_id, int stage_id, int score, int rank )
  {
    ContentValues values = new ContentValues();
    values.put( "score", score );
    values.put( "rank", rank );

    try
    {
      db.update( "STAGE", values, "world_id = ? AND stage_id = ?",
                 new String[]{ String.valueOf( world_id ), String.valueOf( stage_id ) } );
    } catch ( SQLException e )
    {
      Log.e( "save_stage SQL ERROR", e.toString() );
    }


  }

  // アイテムの個数を更新します
  public void save_item( int item_id, int number )
  {
    ContentValues values = new ContentValues();
    values.put( "number", number );

    try
    {
      db.update( "ITEM", values, "item_id = ?", new String[]{String.valueOf( item_id ) });
    } catch ( SQLException e )
    {
      Log.e( "save_item SQL ERROR", e.toString() );
    }


  }

  // 引数を元に設定の値をfloatで返します
  public float load_config( String search_name )
  {

    float value = -1;
    Cursor cursor;

    // 検索条件要変数
    String table = "CONFIG"; // テーブル名
    String[] columns = { "value" }; // 引き出す項目
    String selection = "name = ?"; // 検索条件
    String[] selection_args = { search_name }; //検索条件の"?"部分
    String group_by = null; // groupBy句
    String having = null; // having句
    String order_by = null; // order_by句

    // SQL実行
    try
    {
      cursor = db.query(
        table, columns, selection, selection_args, group_by, having, order_by
      );
      cursor.moveToFirst();

      // 値をvalueに格納
      value = cursor.getFloat( cursor.getColumnIndex( columns[0] ) );
    } catch ( SQLException e )
    {
      Log.e( "load_config SQL ERROR", e.toString() );
    }


    return value;
  }

  // 引数を元にスコアとランクをintの配列で返します
  public int[] load_score( int world_id, int stage_id )
  {
    int[] score_and_rank = { -1, -1 };

    Cursor cursor;

    // 検索条件要変数
    String table = "STAGE"; // テーブル名
    String[] columns = { "score", "rank" }; // 引き出す項目
    String selection = "world_id = ? AND stage_id = ?"; // 検索条件
    String[] selection_args = { String.valueOf( world_id ), String.valueOf( stage_id ) }; //検索条件の"?"部分
    String group_by = null; // groupBy句
    String having = null; // having句
    String order_by = null; // order_by句

    // SQL実行
    try
    {
      cursor = db.query( table, columns, selection, selection_args,
                         group_by, having, order_by);
      cursor.moveToFirst();

      // cursorから配列へスコアとランクを格納
      score_and_rank[ 0 ] = cursor.getInt( cursor.getColumnIndex( "score" ) );
      score_and_rank[ 1 ] = cursor.getInt( cursor.getColumnIndex( "rank" ) );
    } catch ( SQLException e )
    {
      Log.e( "load_score SQL ERROR", e.toString() );
    }


    return score_and_rank;
  }

  // アイテムの所持数を返します
  public int load_item(int item_id)
  {
    int id = -1;
    Cursor cursor;

    // 検索条件要変数
    String table = "ITEM"; // テーブル名
    String[] columns = { "number" }; // 引き出す項目
    String selection = "item_id = ?"; // 検索条件
    String[] selection_args = { String.valueOf( item_id ) }; //検索条件の"?"部分
    String group_by = null; // groupBy句
    String having = null; // having句
    String order_by = null; // order_by句

    // SQL実行
    try
    {

      cursor = db.query( table, columns, selection, selection_args,
                         group_by, having, order_by);
      cursor.moveToFirst();

      id = cursor.getInt( cursor.getColumnIndex( "number" ) );
    } catch ( SQLException e )
    {
      Log.e( "load_item SQL ERROR", e.toString() );
    }


    return id;
  }

  // 初期データを挿入します。
  private void insert_newdata()
  {

    ContentValues values = new ContentValues();

    // TODO:変数仮置き、あとで各項目の取得方法を決めておく
    String[] config_name = { "BGM", "SE" }; // 各設定項目の名前
    float[] config_default = { 50, 50 };
    int[]  stage_value = { 4, 4, 4 }; // ワールドごとのステージ数
    int item_value = 30; // アイテム数

    try
    {
      // 設定分
      for ( int i = 0; i < config_name.length; i++ )
      {
        values.clear();

        values.put( "name", config_name[i] );
        values.put( "value", config_default[i] );

        db.insert( "CONFIG", null, values );
      }

      // スコア分
      for(int world = 0; world < config_default.length; world++)
      {
        for ( int stage = 0; stage < config_default[world]; stage++ )
        {
          values.clear();
          values.put( "world_id", world );
          values.put( "stage_id", stage );
          values.put( "score", 0 );
          values.put( "rank", 0 );

          db.insert( "STAGE", null, values );
        }
      }

      // アイテム分
      for ( int item_count = 0; item_count < item_value; item_count++ )
      {
        values.clear();
        values.put( "item_id", item_count );
        values.put( "number", 0 );

        db.insert( "ITEM", null, values );
      }


    } catch ( SQLException e )
    {
      Log.e( "SQL ERROR", e.toString() );
    }
  }

  public void reset_save()
  {
    String sql;

    try
    {
      // テーブルを削除
      sql = "drop table CONFIG, STAGE, ITEM";
      db.execSQL( sql );

      // CONFIGテーブルを作成
      sql = "create table CONFIG" +
        "(name text not null," +
        "value real not null," +
        "primary key(name))";
      db.execSQL( sql );

      // STAGEテーブルを作成
      sql = "create table STAGE" +
        "(world_id integer not null," +
        "stage_id integer not null," +
        "score integer, not null" +
        "rank integer, not null" +
        "primary key(world_id, stage_id)";
      db.execSQL( sql );

      // ITEMテーブルを作成
      sql = "create table ITEM" +
        "(id integer not null," +
        "number integer not null," +
        "primary key(id))";
      db.execSQL( sql );

      insert_newdata();
    } catch ( SQLException e )
    {
      Log.e( "reset_save()SQL ERROR", e.toString() );
    }
  }



}
