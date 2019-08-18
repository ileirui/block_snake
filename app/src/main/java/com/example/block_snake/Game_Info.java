package com.example.block_snake;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class Game_Info extends Dialog {
    String g="本游戏是将经典的俄罗斯方块和贪吃蛇结合在一起的休闲小游戏",
            g1="本游戏有单机及局域网联机两种模式",g2="llllllll";
    int floge=0;
    public Game_Info(Context context){
        super(context);
        setCancelable(false);   //禁用返回键
        setContentView(R.layout.game_info);
        final TextView textView=findViewById(R.id.game_view);
        final Button next=findViewById(R.id.btn_next);
        final Button previous=findViewById(R.id.btn_previous);
        previous.setEnabled(false);
        Button.OnClickListener b=new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btn_next:
                        switch (floge){
                            case 0:
                                textView.setText(g1);
                                previous.setEnabled(true);
                                floge++;
                                break;
                            case 1:
                                textView.setText(g2);
                                floge++;
                                next.setText("不再提示");
                                previous.setText("关闭");
                                break;
                            case 2:
                                floge=404;
                                dismiss();   //对话框销毁
                                break;
                        }
                        break;
                    case R.id.btn_previous:
                        switch (floge){
                            case 0:
                                break;
                            case 1:
                                floge--;
                                previous.setEnabled(false);
                                textView.setText(g);
                                break;
                            case 2:
                                dismiss();   //对话框销毁
                                break;
                        }
                        break;
                }

            }
        };
        next.setOnClickListener(b);
        previous.setOnClickListener(b);
    }
}
