package com.example.block_snake;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class UserInformation extends AppCompatActivity {
    TextView easy_score,ordinary_score,hard_score,other_score,user_name;
    SQLiteDatabase db;
    DBhelper dBhelper;
    Button chang_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);
        easy_score=findViewById(R.id.easy_score);
        ordinary_score=findViewById(R.id.ordinary_score);
        hard_score=findViewById(R.id.hard_score);
        other_score=findViewById(R.id.other_score);
        chang_name=findViewById(R.id.change_name);
        user_name=findViewById(R.id.user_name);
        dBhelper=new DBhelper(this);

        db=dBhelper.getReadableDatabase();
        int id=1;
        Cursor cursor=db.rawQuery("select * from UserInfo where id=?",new String[]{String.valueOf(id)});
        if (cursor.getCount()!=0){
            cursor.moveToFirst();
            easy_score.setText(cursor.getString(cursor.getColumnIndex("easy")));
            ordinary_score.setText(cursor.getString(cursor.getColumnIndex("ordinary")));
            hard_score.setText(cursor.getString(cursor.getColumnIndex("hard")));
            other_score.setText(cursor.getString(cursor.getColumnIndex("other")));
        }
        db.close();

        user_name.setText(getName(this));
        chang_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeName();
            }
        });
    }
    public void changeName(){
        final changeName c=new changeName(UserInformation.this);
        c.setTitle("请输入名称");
        c.show();
        c.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                user_name.setText(c.name);
                update(1,c.name);
            }
        });
    }

    public void update(int id,String name){
        SQLiteDatabase db;
        DBhelper dBhelper;
        dBhelper=new DBhelper(this);
        db=dBhelper.getWritableDatabase();
        db.execSQL("update UserInfo set name=? where id=?",new Object[]{name,id});
        db.close();
    }

    //获取用户名
    public String getName(Context context){
        SQLiteDatabase db;
        DBhelper dBhelper;
        dBhelper=new DBhelper(context);
        int id=1;
        String name="";
        db=dBhelper.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from UserInfo where id=?",new String[]{String.valueOf(id)});
        if (cursor.getCount()!=0){
            cursor.moveToFirst();
            name=cursor.getString(cursor.getColumnIndex("name"));
        }
        db.close();
        return name;
    }
}
