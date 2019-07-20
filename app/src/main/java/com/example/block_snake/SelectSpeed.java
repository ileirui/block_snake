package com.example.block_snake;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SelectSpeed extends AppCompatActivity implements View.OnClickListener {
    Button btn_easy,btn_ordinary,btn_hard,btn_other,btn_user;
    UserInformation u=new UserInformation();
    TextView user_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_speed);
        btn_easy=findViewById(R.id.btn_easy);
        btn_ordinary=findViewById(R.id.btn_ordinary);
        btn_hard=findViewById(R.id.btn_hard);
        btn_other=findViewById(R.id.btn_other);
        btn_user=findViewById(R.id.user_picture);
        user_name=findViewById(R.id.user_name);
        user_name.setText(u.getName(SelectSpeed.this));

        btn_easy.setOnClickListener(this);
        btn_ordinary.setOnClickListener(this);
        btn_hard.setOnClickListener(this);
        btn_other.setOnClickListener(this);
        btn_user.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent=new Intent(SelectSpeed.this,MainActivity.class);
        switch (v.getId()){
            case R.id.btn_easy:
                intent.putExtra("level",1);
                startActivity(intent);
                break;
            case R.id.btn_ordinary:
                intent.putExtra("level",2);
                startActivity(intent);
                break;
            case R.id.btn_hard:
                intent.putExtra("level",3);
                startActivity(intent);
                break;
            case R.id.btn_other:
                intent.putExtra("level",4);
                startActivity(intent);
                break;
            case R.id.user_picture:
                Intent intent1=new Intent(SelectSpeed.this,UserInformation.class);
                startActivity(intent1);
                break;
        }
    }

}