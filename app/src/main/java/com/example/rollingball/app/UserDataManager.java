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
  }

  // CONFIGの設定情報を更新します
  public void save_config( String name, float value)
  {
    String sql;

    sql = "update CONFIG set value = " + String.valueOf( value ) +
          ", where name = '" + name + "'";
    db.execSQL( sql );


 }

  // 引数のステージデータをセーブします
  public void save_stage( int world_id, int stage_id, int score, int rank )
  {
    ContentValues values = new ContentValues();
    values.put( "score", score );
    values.put( "rank", rank );

    db.update( "STAGE", values, "world_id = ? AND stage_id = ?",
               new String[]{ String.valueOf( world_id ), String.valueOf( stage_id ) } );
  }

  // アイテムの個数を更新します
  public void save_item( int item_id, int number )
  {
    ContentValues values = new ContentValues();
    values.put( "number", number );

    db.update( "ITEM", values, "item_id = ?", new String[]{String.valueOf( item_id ) });

  }

  public void load()
  {
    Cursor cursor;
  }

  // 引数を元に設定の値をfloatで返します
  public float load_config( String search_name )
  {

    float value;
    Cursor cursor;

    // 検索条件要変数
    String table = "CONFIG"; // テーブル名
    String[] colums = { "value" }; // 引き出す項目
    String selection = "name = ?"; // 検索条件
    String[] selection_args = { search_name }; //検索条件の"?"部分
    String group_by = null; // groupBy句
    String having = null; // having句
    String order_by = null; // order_by句

    // SQL実行
    cursor = db.query( table, colums, selection, selection_args,
                       group_by, having, order_by);
    cursor.moveToFirst();

    // 値をvalueに格納
    value = cursor.getFloat( cursor.getColumnIndex( colums[0] ) );

    return value;
  }

  // 引数を元にスコアとランクをintの配列で返します
  public int[] load_score( int world_id, int stage_id )
  {
    int[] score_and_rank = { 0, 0 };

    Cursor cursor;

    // 検索条件要変数
    String table = "STAGE"; // テーブル名
    String[] colums = { "score", "rank" }; // 引き出す項目
    String selection = "world_id = ? AND stage_id = ?"; // 検索条件
    String[] selection_args = { String.valueOf( world_id ), String.valueOf( stage_id ) }; //検索条件の"?"部分
    String group_by = null; // groupBy句
    String having = null; // having句
    String order_by = null; // order_by句

    // SQL実行
    cursor = db.query( table, colums, selection, selection_args,
                       group_by, having, order_by);
    cursor.moveToFirst();

    // cursorから配列へスコアとランクを格納
    score_and_rank[ 0 ] = cursor.getInt( cursor.getColumnIndex( "score" ) );
    score_and_rank[ 1 ] = cursor.getInt( cursor.getColumnIndex( "rank" ) );

    return score_and_rank;
  }

  public int load_item(int item_id)
  {
    int id;
    Cursor cursor;

    // 検索条件要変数
    String table = "ITEM"; // テーブル名
    String[] colums = { "number" }; // 引き出す項目
    String selection = "item_id = ?"; // 検索条件
    String[] selection_args = { String.valueOf( item_id ) }; //検索条件の"?"部分
    String group_by = null; // groupBy句
    String having = null; // having句
    String order_by = null; // order_by句

    // SQL実行
    cursor = db.query( table, colums, selection, selection_args,
                       group_by, having, order_by);
    cursor.moveToFirst();

    id = cursor.getInt( cursor.getColumnIndex( "number" ) );

    return id;
  }

  // 初期データを挿入します。
  private void insert_newdata()
  {

    // TODO:変数仮置き、あとで各項目の取得方法を決めておく
    String[] config_name = { "BGM", "SE" }; // 各設定項目の名前
    int[]  stage_value = { 4, 4, 4 }; // ワールドごとのステージ数
    int item_value = 30; // アイテム数

    try
    {
      for ( String name : config_name )
      {
        // TODO:SQL文書く
      }


    } catch ( SQLException e )
    {
      Log.e( "SQL ERROR", e.toString() );
    }
  }

  public void reset_save(){}



}
