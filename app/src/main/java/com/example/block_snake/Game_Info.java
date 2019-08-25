package com.example.block_snake;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class Game_Info extends Dialog {
    String easy="简单模式",
           ordinary="普通模式",
           hard="困难模式",
           other="噩梦模式",
           create="创建房间来等待好友的加入。开始游戏后，您操控的角色是方块",
           jion="点击加入游戏来搜索并加入已存在的房间或等待好友创建房间。开始游戏后，您操控的角色是小蛇";
    public Game_Info(Context context){
        super(context);
        setCancelable(false);   //禁用返回键
        setContentView(R.layout.game_info);
        final TextView textView=findViewById(R.id.game_view);
        final Button next=findViewById(R.id.btn_next);
        if (SelectSpeed.info_floge==1)
            textView.setText(easy);
        else if (SelectSpeed.info_floge==2)
            textView.setText(ordinary);
        else if (SelectSpeed.info_floge==3)
            textView.setText(hard);
        else if (SelectSpeed.info_floge==4)
            textView.setText(other);
        else if (SelectSpeed.info_floge==5)
            textView.setText(create);
        else if (SelectSpeed.info_floge==6)
            textView.setText(jion);
        Button.OnClickListener b=new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btn_next:
                       break;
                }
                dismiss();
            }
        };
        next.setOnClickListener(b);
    }
}
