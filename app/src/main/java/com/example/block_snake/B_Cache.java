package com.example.block_snake;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;
import java.util.Set;

public class B_Cache {
    String filename;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public B_Cache(Context context,String filename){
        this.filename=filename;
        //数据只能被本程序读写
        sharedPreferences=context.getSharedPreferences(this.filename,Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
    }

    //向Cache存入数据,数据可以是 String,int,Boolean等
    public void putValue(String key,String value){
        editor.putString(key,value);
        editor.commit();  //提交数据
    }

    public void putValue(String key,int value){
        editor.putInt(key,value);
        editor.commit();  //提交数据
    }

    public void putValue(String key,List<String> value){
        editor.putStringSet(key,(Set<String>)value);
        editor.commit();  //提交数据
    }

    public void putValue(String key,Boolean value){
        editor.putBoolean(key,value);
        editor.commit();  //提交数据
    }

    //获取Cache内的数据，如果key不存在则返回默认def
    public String getValue(String key,String def){return sharedPreferences.getString(key,def);}

    //清空Cache
    public void clearCache(){
        editor.clear();
        editor.commit();
    }
}
