package com.example.block_snake;

import android.app.Dialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class changeName extends Dialog {
    String name;
    public changeName(Context context){
        super(context);
        setCancelable(false);   //禁用返回键
        setContentView(R.layout.change_name);
        final EditText uname=findViewById(R.id.e_name);
        final Button ok=findViewById(R.id.ok);
        Button cancle=findViewById(R.id.cancle);
        Button.OnClickListener b=new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.ok:
                        name=uname.getText().toString().trim();
                        break;
                    case R.id.cancle:
                        break;
                }
                dismiss();   //对话框销毁
            }
        };
        ok.setOnClickListener(b);
        cancle.setOnClickListener(b);
    }
}
