package com.example.block_snake;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Login extends AppCompatActivity {
    SQLiteDatabase db;
    DBhelper dBhelper;
    Button btn_login;
    int id =1;
    String name="游客",picture="R.drawable.1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btn_login=findViewById(R.id.btn_login);
        dBhelper=new DBhelper(this);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(id,name,picture);
            }
        });
    }

    public void login(int id,String name,String picture){
        db=dBhelper.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from UserInfo where id=?",new String[]{String.valueOf(id)});
        if (cursor.getCount()!=0){
            db.close();
            Intent intent=new Intent(Login.this,SelectMode.class);
            startActivity(intent);
        } else {
            db=dBhelper.getWritableDatabase();
            db.execSQL("insert into UserInfo(id,name,picture)values(?,?,?)",new Object[]{id,name,picture});
            db.close();
            Intent intent=new Intent(Login.this,SelectMode.class);
            startActivity(intent);
        }
    }

}
