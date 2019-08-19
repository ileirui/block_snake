package com.example.block_snake;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Login extends AppCompatActivity {
    SQLiteDatabase db;
    DBhelper dBhelper;
    Button btn_login;
    int id =1,score=0,f=0;
    String name="游客";
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {"android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btn_login=findViewById(R.id.btn_login);
        dBhelper=new DBhelper(this);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login.this,SelectMode.class);
                startActivity(intent);
            }
        });
        verifyStoragePermissions(this);
        login(id,name);
        if (getfloge()==0)
            game_info();
    }

    public void login(int id,String name){
        db=dBhelper.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from UserInfo where id=?",new String[]{String.valueOf(id)});
        if (cursor.getCount()!=0){
            db.close();
        } else {
            db=dBhelper.getWritableDatabase();
            db.execSQL("insert into UserInfo(id,name,floge,easy,ordinary,hard,other,more,music)values(?,?,?,?,?,?,?,?,?)",new Object[]{id,name,score,score,score,score,score,score,score});
            db.close();
        }
    }

    //是否按下返回键
    public void onBackPressed() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("是否退出游戏？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
            }
        });
        builder.setNegativeButton("返回", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create();
        builder.show();
    }

    //获取读写权限
    public static void verifyStoragePermissions(Activity activity) {
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void game_info(){
        final Game_Info game_info=new Game_Info(Login.this);
        game_info.setTitle("游戏简介");
        game_info.show();
        game_info.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (game_info.floge==404){
                    setfloge(1,1);
                }
            }
        });
    }

    public void setfloge(int floge,int id){
        db=dBhelper.getWritableDatabase();
        db.execSQL("update UserInfo set floge=? where id=?",new Object[]{floge,id});
        db.close();
    }

    public int getfloge(){
        db=dBhelper.getReadableDatabase();
        int id=1;
        Cursor cursor=db.rawQuery("select * from UserInfo where id=?",new String[]{String.valueOf(id)});
        if (cursor.getCount()!=0) {
            cursor.moveToFirst();
//            fl = Integer.parseInt(cursor.getString(cursor.getColumnIndex("floge")));
        }
        return Integer.parseInt(cursor.getString(cursor.getColumnIndex("floge")));
    }
}
