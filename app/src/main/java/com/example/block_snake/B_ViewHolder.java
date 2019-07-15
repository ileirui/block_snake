package com.example.block_snake;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class B_ViewHolder {
    private final SparseArray<View> mViews;
    private final View mConvertView;
    private int mPosition;

    public B_ViewHolder(Context context, ViewGroup viewGroup,int layoutId,int position){
        this.mPosition=position;
        this.mViews=new SparseArray<>();
        mConvertView= LayoutInflater.from(context).inflate(layoutId,viewGroup,false);
        mConvertView.setTag(this);
    }

    //得到一个B_ViewHolder对象
    public static B_ViewHolder get(Context context,View view,ViewGroup viewGroup,int layoutId,int position){
        return (new B_ViewHolder(context,viewGroup,layoutId,position));
    }

    //通过控件的ID获取对应控件，没有则加入view
    public <T extends View> T getView(int layoutId){
        View view=mViews.get(layoutId);
        if (view==null){
            view=mConvertView.findViewById(layoutId);
            mViews.put(layoutId,view);
        }
        return (T) view;
    }

    public View getConvertView(){return mConvertView;}
}
