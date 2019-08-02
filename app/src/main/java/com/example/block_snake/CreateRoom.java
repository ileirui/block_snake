package com.example.block_snake;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CreateRoom extends AppCompatActivity {
    Button btn_create,btn_back,btn_join;
    TextView user_name;
    ImageView btn_picture;
    UserInformation u=new UserInformation();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room);
        btn_back=findViewById(R.id.btn_back);
        btn_create=findViewById(R.id.btn_create);
        btn_join=findViewById(R.id.btn_join);
        user_name=findViewById(R.id.user_name);
        btn_picture=findViewById(R.id.user_picture);
        user_name.setText(u.getName(CreateRoom.this));
        btn_picture.setImageBitmap(u.getBitmip());

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CreateRoom.this,SelectMode.class);
                startActivity(intent);
            }
        });
        btn_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CreateRoom.this,UserInformation.class);
                startActivity(intent);
            }
        });
    }

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
}
