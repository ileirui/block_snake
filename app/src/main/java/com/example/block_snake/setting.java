package com.example.block_snake;

import android.app.Dialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;

public class setting extends Dialog {
    String music1;
    int m;
    public setting(Context context){
        super(context);
        setCancelable(false);   //禁用返回键
        setContentView(R.layout.setting);
        final CheckBox cm1=findViewById(R.id.check_music1);
        final CheckBox cm2=findViewById(R.id.check_music2);
        final Button ok=findViewById(R.id.ok);
        final Button cancle=findViewById(R.id.cancle);
        if (MainActivity.music==0) {
            cm1.setChecked(true);
            cm2.setChecked(false);
        }
        else if (MainActivity.music==1) {
            cm2.setChecked(true);
            cm1.setChecked(false);
        }
        else if (MainActivity.music==2){
            cm1.setChecked(false);
            cm2.setChecked(false);
        }
        Button.OnClickListener b=new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.ok:
                       if (cm1.isChecked()&&cm2.isChecked()==false) {
                            music1 = "begin";
                            m=0;
                        }
                        else if (cm1.isChecked()==false&&cm2.isChecked()){
                           music1 = "begin";
                           m=1;
                       }
                       else if (cm1.isChecked()&&cm2.isChecked()){
                           music1="begin";
                           m=0;
                       }
                        else {
                            music1 = "end";
                            m=2;
                        }
                        break;
                    case R.id.cancle:
                        music1="out";
                        break;
                }
                dismiss();   //对话框销毁
            }
        };
        ok.setOnClickListener(b);
        cancle.setOnClickListener(b);
    }
}
