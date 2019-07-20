package com.example.block_snake;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SelectMode extends AppCompatActivity {
    Button btn_dj,btn_picture;
    TextView user_name;
    UserInformation u=new UserInformation();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_mode);
        btn_dj=findViewById(R.id.btn_danji);
        btn_picture=findViewById(R.id.user_picture);
        user_name=findViewById(R.id.user_name);
        user_name.setText(u.getName(SelectMode.this));
        btn_dj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SelectMode.this,SelectSpeed.class);
                startActivity(intent);
            }
        });
        btn_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SelectMode.this,UserInformation.class);
                startActivity(intent);
            }
        });
    }
}
