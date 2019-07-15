package com.example.block_snake;
//有关adapter的封装
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class B_CommonAdapter<T> extends BaseAdapter {
    private final LayoutInflater mInflater;
    private final int mItemLayoutId;
    Context mContext;
    List<T> mDatas;

    public B_CommonAdapter(Context context, List<T> mDatas, int mItemLayoutId){
        mInflater=LayoutInflater.from(context);
        this.mContext=context;
        this.mDatas=mDatas;
        this.mItemLayoutId=mItemLayoutId;
    }

    //设置数据
    public void b_setData(List<T> datas){
        if (datas!=null){
            mDatas=datas;
            notifyDataSetChanged();  //BaseAdapter自带函数
        }
    }

    //添加数据
    public void b_addData(List<T> datas){
        if (datas!=null){
            mDatas.addAll(datas);
            notifyDataSetChanged();
        }
    }

    public void b_addData(T data){
        if (data!=null){
            mDatas.add(data);
            notifyDataSetChanged();
        }
    }

    //删除数据
    public void b_deleteData(T data){
        if (mDatas!=null){
            mDatas.remove(data);
        }
    }

    //清空数据
    public void b_clearData(){
        if (mDatas!=null){
            mDatas.clear();
            notifyDataSetChanged();
        }
    }

    //获得数据
    protected List<T> getData(){return mDatas;}

    public T getItem(int posotion){return mDatas.get(posotion);}

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public int getCount() { return mDatas.size(); }

    public B_ViewHolder getB_ViewHolder(int position,View view,ViewGroup parent){
        return B_ViewHolder.get(mContext,view,parent,mItemLayoutId,position);
    }

    public abstract void convert(B_ViewHolder Helper,T item);

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final B_ViewHolder b_viewHolder=getB_ViewHolder(position,convertView,parent);
        convert(b_viewHolder,getItem(position));
        return b_viewHolder.getConvertView();
    }

    public void setmDatas(List<T> mDatas) { this.mDatas = mDatas; }
}
