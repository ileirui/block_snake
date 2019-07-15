package com.example.block_snake;

import android.content.Context;
import android.widget.ImageView;
import android.graphics.Color;

import java.util.List;

public class B_Adapter extends B_CommonAdapter {
    Context context;
    List<Integer> mDatas;

    public B_Adapter(Context context,List mDatas,int mlayoutId){
        super(context,mDatas,mlayoutId);
        this.context=context;
        this.mDatas=mDatas;
    }

    @Override
    public void convert(B_ViewHolder helper, Object item) {
        ImageView imageView=helper.getView(R.id.adapter_image);
        Integer integer=(Integer) item;
        if (integer>0){
            imageView.setImageResource(B_Shape.color[integer-1]);
        }
        else
            imageView.setBackgroundColor(Color.parseColor("#29505B"));
    }
}
