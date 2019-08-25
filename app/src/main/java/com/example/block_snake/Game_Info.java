package com.example.block_snake;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class Game_Info extends Dialog {
    String easy="简单模式:小蛇可以穿越墙体，也可以撞击下落完成的方块，撞击后，蛇身减一，对应方块消失同时获得相应的分数(注意，小蛇最小长度为三，若此时在撞击方块，会游戏失败哦)",
           ordinary="普通模式：小蛇不可以穿越墙体，但可以撞击下落完成的方块，撞击后，蛇身减一，对应方块消失同时获得相应的分数(注意，小蛇最小长度为三，若此时在撞击方块，会游戏失败哦)",
           hard="困难模式：小蛇不可以穿越墙体，但可以撞击下落完成的方块，同时方块的旋转模式变为随机旋转模式(随机旋转模式：点击按钮后，有可能旋转出其他形状的方块)",
           other="噩梦模式：小蛇不可以穿越墙体，也不可以撞击下落完成的方块，方块的旋转模式变为随机旋转模式(随机旋转模式：点击按钮后，有可能旋转出其他形状的方块)",
           create="创建房间来等待好友的加入。开始游戏后，您操控的角色是方块(双人模式下，游戏难度为简单难度)",
           jion="点击加入游戏来搜索并加入已存在的房间或等待好友创建房间。开始游戏后，您操控的角色是小蛇(双人模式下，游戏难度为简单难度)";
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
