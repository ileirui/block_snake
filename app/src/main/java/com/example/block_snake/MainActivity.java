package com.example.block_snake;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    TextView t_level,t_score,t_highestScore;    //等级和分数标签,最高分标签
    int timeInterval=800;    //时间间隔
    B_Adapter block,nextBlock;     //方块及下一方块
    B_Cache b_cache;         //方块缓存
    int[][] b_color=new int[15][10];  //方块颜色
    int score=0,highestScore=0;             //分数，最高分
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
    int nextRand,nextRandColor;
    int stop=0;

    //方块下落线程
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            //将下落完成的方块存入blockList中
            for (int i = 0; i < ySize; i++) {
                if (allBlock[i] == 0) {
                    for (int j = 0; j < xSize; j++) {
                        blockList.set(i * xSize + j, 0);
                    }
                } else {
                    for (int j = 0; j < xSize; j++) {
                        blockList.set(i * xSize + j, b_color[i][j]);
                    }
                }
            }

            //方块是否能继续向下移动
            boolean canMove = true;
            if (msg.what == 0) {
                position[0]++;   //先下移一行，做后面的判断是否能下移，不能则返回
                for (int i = 3; i >= 0; i--) {
                    int line = i + position[0];
                    if (line >= 0 && B_Shape.shape[rand][i] != 0) {
                        //判断是否超行或者下面有方块
                        if (line >= ySize || ((allBlock[line] & (leftMath(B_Shape.shape[rand][i], position[1]))) != 0)) {
                            canMove = false;
                            break;
                        }
                    }
                }
                if (!canMove) {
                    position[0]--;   //上面进行了假设下移，所以此处要移回原位置
                    for (int i = 3; i >= 0; i--) {
                        int line = i + position[0];
                        if (line >= 0 && B_Shape.shape[rand][i] != 0) {
                            for (int j = 0; j < xSize; j++) {
                                if (((1 << j) & (leftMath(B_Shape.shape[rand][i], position[1]))) != 0) {
                                    blockList.set(line * xSize + j, randColor);
                                }
                            }
                        }
                    }
                    stopDown();
                } else {
                    for (int i = 3; i >= 0; i--) {
                        int line = i + position[0];
                        if (line >= 0 && B_Shape.shape[rand][i] != 0) {
                            for (int j = 0; j < xSize; j++) {
                                if (((1 << j) & (leftMath(B_Shape.shape[rand][i], position[1]))) != 0) {
                                    blockList.set(line * xSize + j, randColor);
                                }
                            }
                        }
                    }
                }
            } else {
                for (int i = 3; i >= 0; i--) {
                    int line = i + position[0];
                    if (line >= 0 && B_Shape.shape[rand][i] != 0) {
                        for (int j = 0; j < xSize; j++) {
                            if (((1 << j) & (leftMath(B_Shape.shape[rand][i], position[1]))) != 0) {
                                blockList.set(line * xSize + j, randColor);
                            }
                        }
                    }
                }
            }

            block.setmDatas(blockList);
            block.notifyDataSetChanged();
        }
    };

    //移位函数
    int leftMath(int a,int b){
        if (b<0)
            return a>>-b;   //a右移-b位
        else
            return a<<b;    //a左移b位
    }

    //写入，消除，重置
    void stopDown(){
        //写入，将新一轮下落完成后的已存在的方块写入到allBlock中
        for (int i=3;i>=0;i--){
            int line=i+position[0];
            if (line>=0&&B_Shape.shape[rand][i]!=0){
                allBlock[line]+=(leftMath(B_Shape.shape[rand][i],position[1]));
                for (int j=0;j<xSize;j++){
                    if (((1<<j)&(leftMath(B_Shape.shape[rand][i],position[1])))!=0){
                        b_color[line][j]=randColor;
                    }
                }
            }
        }

        for (int i=ySize-1;i>0;){
            //判断这一行是否已满
            if (allBlock[i]==0x3ff){
                score++;
                t_score.setText("分数: "+score);
                for (int j=i-1;j>0;j--){
                    //满的话消除方块，剩下的方块顺序下降
                    allBlock[j+1]=allBlock[j];
                    for (int k=0;k<xSize;k++){
                        b_color[j+1][k]=b_color[j][k];
                    }
                }
                allBlock[0]=0;
                for (int j=0;j<xSize;j++){
                    b_color[0][j]=0;
                }
            }else {
                //若未满，则判断下一行
                i--;
            }
        }

        //若最后一行也满了，则游戏失败，保存数据
        if (allBlock[0]!=0){
            if (score>highestScore){
                b_cache.getValue("highestScore",score+"");
                highestScore=score;
                t_highestScore.setText("最高分: "+highestScore);
                t_score.setText("分数: "+score);
            }
            gameOver();
        }

        rand=nextRand;
        position[0]=B_Shape.initPosition[rand][1]; //重新初始化方块位置
        position[1]=B_Shape.initPosition[rand][0];
        randColor=nextRandColor;

        nextRand=random.nextInt(19);
        nextRandColor=random.nextInt(5)+1;
        nextBlockShow();
    }

    //启动计时器，即开始游戏
    private void startTimer(){
        if (timer==null)
            timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        },0,timeInterval);
    }

    //停止计时器
    private void stopTimer(){
        if (timer!=null){
            timer.cancel();
            timer=null;   //一定设为null，否则计时器不会收回
        }
    }

    //下一跳方块
    private void nextBlockShow(){
        blockNextList.clear();
        for (int i=0;i<4;i++){
            for (int j=0;j<4;j++){
                if (((1<<j)&B_Shape.shape[nextRand][i])!=0){
                    blockNextList.add(nextRandColor);
                }else{
                    blockNextList.add(0);
                }
            }
        }
        nextBlock.setmDatas(blockNextList);
        nextBlock.notifyDataSetChanged();
    }

    //游戏结束
    private void gameOver(){
        b_cache.putValue("highestScore",String.valueOf(highestScore));
        stopTimer();
        //创建弹窗
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("游戏结束");
        builder.setMessage("本局得分: "+score);
        //在弹窗中设置  再来一局  按钮
        builder.setPositiveButton("再来一局", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                stop =0;
                position[0]=-4;  //初始化方块位置
                position[1]=4;
                for (int i=0;i<ySize;i++){
                    allBlock[i]=0;
                    for (int j=0;j<xSize;j++){
                        b_color[i][j]=0;
                    }
                }
                rand=random.nextInt(19);
                position[0]=B_Shape.initPosition[rand][1];
                position[1]=B_Shape.initPosition[rand][0];
                randColor=random.nextInt(5)+1;
                nextRand=random.nextInt(19);
                nextRandColor=random.nextInt(5)+1;

                timer=new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(0);
                    }
                },0,timeInterval);
            }
        });

        //退出  按钮
        builder.setNegativeButton("退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).create();

        builder.setCancelable(false);
        builder.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopTimer();
    }

    //缓存及控件初始化函数
    public void init(){
        //初始化缓存并尝试获取数据
        b_cache=new B_Cache(MainActivity.this,"userInfo");
        String hScore="";
        try {
            hScore=b_cache.getValue("highestScore",String.valueOf(0));
        }catch (Exception e){
            Log.e("MainActivity",e.toString());
        }

        //尝试将最高分准换成整型
        try{
            highestScore=Integer.parseInt(hScore.toString());
        }catch (NumberFormatException e){
            highestScore=0;
        }

        //控件初始化
        t_score=findViewById(R.id.score);
        t_highestScore=findViewById(R.id.highestScore);
        t_level=findViewById(R.id.level);
        b_btn_down=findViewById(R.id.b_btn_down);
        b_btn_left=findViewById(R.id.b_btn_left);
        b_btn_right=findViewById(R.id.b_btn_right);
        b_btn_up=findViewById(R.id.b_btn_up);
        t_score.setText("分数: "+score);
        t_highestScore.setText("最高分: "+highestScore);
        b_s_view=findViewById(R.id.b_s_view);
        b_next_view=findViewById(R.id.b_next_view);

        for (int i=0;i<10;i++){
            for (int j=0;j<15;j++){
                blockList.add(0);
            }
        }

        //获得方块属性并显示在画布上
        block=new B_Adapter(MainActivity.this,blockList,R.layout.layout);
        b_s_view.setAdapter(block);

        random=new Random();
        rand=random.nextInt(19);
        position[0]=B_Shape.initPosition[rand][1];
        position[1]=B_Shape.initPosition[rand][0];
        randColor=random.nextInt(5)+1;
        nextRand=random.nextInt(19);
        nextRandColor=random.nextInt(5)+1;
        blockNextList.clear();

        for (int i=0;i<4;i++){
            for (int j=0;j<4;j++){
                if (((1<<j)&B_Shape.shape[rand][i])!=0){
                    blockNextList.add(nextRandColor);
                }else {
                    blockNextList.add(0);
                }
            }
        }

        nextBlock=new B_Adapter(MainActivity.this,blockNextList,R.layout.layout);
        b_next_view.setAdapter(nextBlock);

        timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        },0,timeInterval);
    }

    //移动按钮监听事件
    public void btn_Move(){
        //方块左移按钮
        b_btn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i=3;i>=0;i--){
                    //如果右移之后再左移不等于原来的数，则不可移动
                    if ((((leftMath(B_Shape.shape[rand][i],position[1]))>>1)<<1)!=(leftMath(B_Shape.shape[rand][i],position[1]))){
                        return;
                    }
                }

                for (int i=3;i>=0;i--){
                    int line=i+position[0];
                    if (line>=0&&B_Shape.shape[rand][i]!=0){
                        if ((allBlock[line]&(leftMath(B_Shape.shape[rand][i],position[1])>>1))!=0){
                            return;
                        }
                    }
                }
                position[1]--;
                handler.sendEmptyMessage(1);
            }
        });
        //方块右移按钮
        b_btn_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i=3;i>=0;i--){
                    if (((leftMath(B_Shape.shape[rand][i],position[1]))<<1)>0x3ff){
                        return;  //右移越界，退出
                    }
                }
                for (int i=3;i>=0;i--){
                    int line=i+position[0];
                    if (line>=0&&B_Shape.shape[rand][i]!=0){
                        if ((allBlock[line]&(leftMath(B_Shape.shape[rand][i],position[1])<<1))!=0){
                            return;
                        }
                    }
                }
                position[1]++;
                handler.sendEmptyMessage(1);
            }
        });
        //方块旋转按钮
        b_btn_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nextB=B_Shape.nextshape[rand];
                for (int i=3;i>=0;i--){
                    int line =i+position[0];
                    //检查是否越界
                    if (leftMath(B_Shape.shape[nextB][i],position[1])>0x3ff){
                        return;  //右边界
                    }else if (B_Shape.shape[nextB][i]>0&&line>=ySize){
                        return;   //下边界
                    }else if (leftMath(leftMath(B_Shape.shape[nextB][i],position[1]),-position[1])!=B_Shape.shape[nextB][i]){
                        return;   //左边界
                    }else if (line>0&&line<ySize&&(leftMath(B_Shape.shape[nextB][i],position[1])&allBlock[line])!=0){
                        return;   //检查是否与其他方块重合
                    }
                }
                rand=nextB;
                handler.sendEmptyMessage(1);
            }
        });
        //方块下移按钮
        b_btn_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int down =1<<10;
                for (int i=3;i>=0;i--){
                    int line=i+position[0];
                    if (line>=0&&B_Shape.shape[rand][i]!=0){
                        down=Math.min(down,ySize-line-1);
                        for (int j=0;j<xSize;j++){
                            if (((1<<j)&(leftMath(B_Shape.shape[rand][i],position[1])))!=0){
                                for (int k=0;k+line<ySize;k++){
                                    if (b_color[k+line][j]>0){
                                        down=Math.min(down,k-1);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                if (down<=0||down==(1<<10)){
                    return;
                }else{
                    position[0]+=down;
                    handler.sendEmptyMessage(0);
                }
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        btn_Move();
    }
}
