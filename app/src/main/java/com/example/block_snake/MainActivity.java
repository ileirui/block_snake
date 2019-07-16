package com.example.block_snake;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;

public class MainActivity extends AppCompatActivity {
    TextView t_level,t_score;    //等级和分数标签
    int timeInterval=800;    //时间间隔
    B_Adapter block,nextBlock;     //方块及下一方块
    B_Cache b_cache;         //方块缓存
    int[][] b_color=new int[15][10];  //方块颜色
    int score=0;             //分数
    Random random;           //随机变量
    int[] position=new int[]{-4,4};  //方块位置，position[0]为y轴位置
    Timer timer;             //时间变量
    Button b_btn_up,b_btn_down,b_btn_left,b_btn_right,s_btn_up,s_btn_down,s_btn_left,s_btn_right;  //移动按钮
    //画布的格子数为10x15；
    int ySize=15;
    int xSize=10;
    int[] allBlock=new int[ySize];  //用以表示方块的数组
    GridView b_s_view,b_next_view;       //主画布及方块下一跳画布
    List<Integer> blockList=new ArrayList<>();   //方块列表
    List<Integer> blockNextList=new ArrayList<>();  //方块下一跳列表
    int randColor;           //随机颜色
    int rand;
    int nextRand,nextTandColor;

    //方块下落线程


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
