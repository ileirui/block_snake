package com.example.block_snake;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class UserInformation extends AppCompatActivity {
    TextView easy_score,ordinary_score,hard_score,other_score,more_score,user_name;
    Bitmap head;
    ImageView im_h;
    private static String path = "/sdcard/myHead/";
    SQLiteDatabase db;
    DBhelper dBhelper;
    Button chang_name,btn_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);
        easy_score=findViewById(R.id.easy_score);
        ordinary_score=findViewById(R.id.ordinary_score);
        hard_score=findViewById(R.id.hard_score);
        other_score=findViewById(R.id.other_score);
        more_score=findViewById(R.id.more_score);
        chang_name=findViewById(R.id.change_name);
        user_name=findViewById(R.id.user_name);
        btn_back=findViewById(R.id.btn_back);
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
            more_score.setText(cursor.getString(cursor.getColumnIndex("more")));
        }
        db.close();

        user_name.setText(getName(this));
        chang_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeName();
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UserInformation.this,SelectMode.class);
                startActivity(intent);
            }
        });
        initView();
        initListener();
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

    public void initView(){
        im_h=findViewById(R.id.user_picture);
        Bitmap bt = BitmapFactory.decodeFile(path + "head.jpg");// 从SD卡中找头像，转换成Bitmap
        if (bt != null) {
            @SuppressWarnings("deprecation")
            Drawable drawable = new BitmapDrawable(bt);// 转换成drawable
            im_h.setImageDrawable(drawable);
        }
    }

    public void initListener(){
        im_h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.user_picture:// 更换头像
                        Intent intent1 = new Intent(Intent.ACTION_PICK, null);
                        intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        startActivityForResult(intent1, 1);
                        break;
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    cropPhoto(data.getData());// 裁剪图片
                }
                break;
            case 2:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    head = extras.getParcelable("data");
                    if (head != null) {
                        setPicToView(head);// 保存在SD卡中
                        im_h.setImageBitmap(head);// 用ImageView显示出来
                    }
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void cropPhoto(Uri uri){
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 70);
        intent.putExtra("outputY", 70);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 2);
    }

    private void setPicToView(Bitmap bitmap){
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            return;
        }
        FileOutputStream b = null;
        File file = new File(path);
        file.mkdirs();// 创建文件夹
        String fileName = path + "head.jpg";// 图片名字
        try {
            b = new FileOutputStream(fileName);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭流
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Bitmap getBitmip(){
        String path="/sdcard/myHead/head.jpg";
        Bitmap bitmap= BitmapFactory.decodeFile(path);
        return bitmap;
    }
}
