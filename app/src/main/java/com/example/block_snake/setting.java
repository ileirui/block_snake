package com.example.block_snake;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class setting extends Dialog {
    String music;
    boolean m;
    public setting(Context context){
        super(context);
        setCancelable(false);   //禁用返回键
        setContentView(R.layout.setting);
        final CheckBox cm=findViewById(R.id.check_music);
        final Button ok=findViewById(R.id.ok);
        final Button cancle=findViewById(R.id.cancle);
        Button.OnClickListener b=new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.ok:
                       if (cm.isChecked()) {
                            music = "begin";
                            m=true;
                        }
                        else {
                            music = "end";
                            m=false;
                        }
                        break;
                    case R.id.cancle:
                        music="out";
                        break;
                }
                dismiss();   //对话框销毁
            }
        };
        ok.setOnClickListener(b);
        cancle.setOnClickListener(b);
    }
}
