package com.example.rollingball.app;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SaveDataSQLiteOpenHelper extends SQLiteOpenHelper
{

  final static int DATABASE_VERSION = 1;

  public SaveDataSQLiteOpenHelper(Context context)
  {
    super(context, "SAVEDATA", null, DATABASE_VERSION);
  }

  // データベースが作成された時に呼び出されます
  @Override
  public void onCreate( final SQLiteDatabase db )
  {
    String sql;

    try
    {
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

    } catch ( SQLException e )
    {
      Log.e( "SQL ERROR", e.toString() );
    }



  }

  @Override
  public void onUpgrade( final SQLiteDatabase sqLiteDatabase, final int old_version, final int new_version )
  {

  }

}
