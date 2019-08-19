package com.example.block_snake;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBhelper extends SQLiteOpenHelper {
    public DBhelper(Context context){super(context,"blockAsnack",null,1);}

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table UserInfo(id integer primary key,name char(20)," +
                "floge integer,easy integer,ordinary integer,hard integer,other integer,more integer,music integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}
