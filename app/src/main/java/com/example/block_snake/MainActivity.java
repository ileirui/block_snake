package com.example.block_snake;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    TextView t_level,t_score,t_highestScore;    //等级和分数标签,最高分标签
    int timeInterval=800;    //时间间隔
    B_Adapter block,nextBlock;     //方块及下一方块
    int[][] b_color=new int[15][10];  //方块颜色
    int score=0,highestScore=0,level;             //分数，最高分,等级
    Random random;           //随机变量
    int[] position=new int[]{-4,4};  //方块位置，position[0]为y轴位置
    Timer timer;             //时间变量
    Button b_btn_up,b_btn_down,b_btn_left,b_btn_right,s_btn_up,s_btn_down,s_btn_left,s_btn_right,btn_pause,btn_setting;  //移动按钮
    //画布的格子数为10x15；
    int ySize=15;
    int xSize=10;
    int[] allBlock=new int[ySize];  //用以表示方块的数组
    GridView b_s_view,b_next_view;       //主画布及方块下一跳画布
    List<Integer> blockList=new ArrayList<>();   //方块列表
    List<Integer> blockNextList=new ArrayList<>();  //方块下一跳列表
    MediaPlayer mediaPlayer;           //背景音乐
    public static int music;

//---------------------------------------------------
    LinkedList<S_node> snakeBody;
    LinkedList<S_node> snakeBodyC ;
    int direction=12;
    int S_up=10,S_down=11,S_left=12,S_right=13;
    S_node food;
    int status=0;
    boolean refresh=true;

    CreateRoom createRoom = new CreateRoom();

    ServerSocket mServerSocket;
    public Thread server=null;
    public Thread client=null;
    boolean sendAllblock=false;
    boolean GameOver=false;

    //-----------------------------------------------------

    int randColor;           //随机颜色
    int rand;
    int nextRand,nextRandColor;
    SQLiteDatabase db;  //数据库连接变量
    DBhelper dBhelper;
    boolean p=false;   //暂停按钮控制变量


    //方块下落线程
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(!(CreateRoom.Mode==1&&SelectMode.intnetMode==1)) {
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


           }
            //---------------------------------------------------------------------------------------------------------
            if(!(CreateRoom.Mode==0&&SelectMode.intnetMode==1)) {
                S_easy();
            }
            if(SelectMode.intnetMode==1&&CreateRoom.Mode==1){
            client=new Thread(ClientListener);
            client.start();
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
                nextBlock.setmDatas(blockNextList);
                nextBlock.notifyDataSetChanged();
                if(GameOver==true){
                    gameOver();
                }
            }
            if(SelectMode.intnetMode==1&&CreateRoom.Mode==0&&server==null){
                mServerSocket=CreateRoom.mServerSocket;
                server=new Thread(ServerListener);
                server.start();
            }
            if(SelectMode.intnetMode==1&&CreateRoom.Mode==0){
                if(!refresh){
                    gameOver();
                }
            }


            if(refresh) {
                for (S_node node : snakeBody)
                    blockList.set(node.getNodeY() * xSize + node.getNodeX(), 7);
                blockList.set(snakeBody.getFirst().getNodeY() * xSize + snakeBody.getFirst().getNodeX(), 6);

            }
            blockList.set(food.getNodeY()*xSize+food.getNodeX(),7);
            //---------------------------------------------------------------------------------------------------------------

            block.setmDatas(blockList);
            block.notifyDataSetChanged();
        }
    };
    //---------------------------------------------

    void S_easy(){
        switch (direction) {
            case 13:
                snakeBody.addFirst(new S_node(snakeBody.getFirst().getNodeX() + 1, snakeBody.getFirst().getNodeY()));
                if (snakeBody.getFirst().getNodeX() > 9) {
                    if (score > getHighestScore(level)) {
                        highestScore = score;
                        t_highestScore.setText("最高分: " + highestScore);
                        t_score.setText("分数: " + score);
                    }
                    gameOver();
                }
                if ((allBlock[snakeBody.getFirst().getNodeY()] & (int) Math.pow(2, snakeBody.getFirst().getNodeX())) != 0) {
                    snakeBody.removeLast();
                    allBlock[snakeBody.getFirst().getNodeY()] -= (int) Math.pow(2, snakeBody.getFirst().getNodeX());
                    // blockList.set(snakeBody.getFirst().getNodeY()*xSize+snakeBody.getFirst().getNodeX(),0);
                    b_color[snakeBody.getFirst().getNodeY()][snakeBody.getFirst().getNodeX()] = 0;
                    sendAllblock=true;
                }
                S_eat();
                break;
            case 12:
                snakeBody.addFirst(new S_node(snakeBody.getFirst().getNodeX() - 1, snakeBody.getFirst().getNodeY()));
                if (snakeBody.getFirst().getNodeX() < 0) {
                    if (score > getHighestScore(level)) {
                        highestScore = score;
                        t_highestScore.setText("最高分: " + highestScore);
                        t_score.setText("分数: " + score);
                    }
                    gameOver();
                }
                if ((allBlock[snakeBody.getFirst().getNodeY()] & (int) Math.pow(2, snakeBody.getFirst().getNodeX())) != 0) {
                    snakeBody.removeLast();
                    allBlock[snakeBody.getFirst().getNodeY()] -= (int) Math.pow(2, snakeBody.getFirst().getNodeX());
                    //blockList.set(snakeBody.getFirst().getNodeY()*xSize+snakeBody.getFirst().getNodeX(),0);
                    b_color[snakeBody.getFirst().getNodeY()][snakeBody.getFirst().getNodeX()] = 0;
                    sendAllblock=true;
                }
                S_eat();
                break;
            case 10:
                snakeBody.addFirst(new S_node(snakeBody.getFirst().getNodeX(), snakeBody.getFirst().getNodeY() - 1));
                if (snakeBody.getFirst().getNodeY() < 0) {
                    if (score > getHighestScore(level)) {
                        highestScore = score;
                        t_highestScore.setText("最高分: " + highestScore);
                        t_score.setText("分数: " + score);
                    }
                    gameOver();
                } else {
                    if ((allBlock[snakeBody.getFirst().getNodeY()] & (int) Math.pow(2, snakeBody.getFirst().getNodeX())) != 0) {
                        snakeBody.removeLast();
                        allBlock[snakeBody.getFirst().getNodeY()] -= (int) Math.pow(2, snakeBody.getFirst().getNodeX());
                        //blockList.set(snakeBody.getFirst().getNodeY()*xSize+snakeBody.getFirst().getNodeX(),0);
                        b_color[snakeBody.getFirst().getNodeY()][snakeBody.getFirst().getNodeX()] = 0;
                        sendAllblock=true;
                    }
                }
                S_eat();
                break;
            case 11:
                snakeBody.addFirst(new S_node(snakeBody.getFirst().getNodeX(), snakeBody.getFirst().getNodeY() + 1));
                if (snakeBody.getFirst().getNodeY() > 14) {
                    if (score > getHighestScore(level)) {
                        highestScore = score;
                        t_highestScore.setText("最高分: " + highestScore);
                        t_score.setText("分数: " + score);
                    }
                    gameOver();
                } else {
                    if ((allBlock[snakeBody.getFirst().getNodeY()] & (int) Math.pow(2, snakeBody.getFirst().getNodeX())) != 0) {
                        snakeBody.removeLast();
                        allBlock[snakeBody.getFirst().getNodeY()] -= (int) Math.pow(2, snakeBody.getFirst().getNodeX());
                        //blockList.set(snakeBody.getFirst().getNodeY()*xSize+snakeBody.getFirst().getNodeX(),0);
                        b_color[snakeBody.getFirst().getNodeY()][snakeBody.getFirst().getNodeX()] = 0;
                        sendAllblock=true;
                    }
                }
                S_eat();
                break;
        }
    }
    void S_other(){
        switch (direction) {
            case 13:
                snakeBody.addFirst(new S_node(snakeBody.getFirst().getNodeX() + 1, snakeBody.getFirst().getNodeY()));
                if (snakeBody.getFirst().getNodeX() > 9) {
                    if (score > getHighestScore(level)) {
                        highestScore = score;
                        t_highestScore.setText("最高分: " + highestScore);
                        t_score.setText("分数: " + score);
                    }
                    gameOver();
                }
                if ((allBlock[snakeBody.getFirst().getNodeY()] & (int) Math.pow(2, snakeBody.getFirst().getNodeX())) != 0) {
                    gameOver();
                    sendAllblock=true;
                }
                S_eat();
                break;
            case 12:
                snakeBody.addFirst(new S_node(snakeBody.getFirst().getNodeX() - 1, snakeBody.getFirst().getNodeY()));
                if (snakeBody.getFirst().getNodeX() < 0) {
                    if (score > getHighestScore(level)) {
                        highestScore = score;
                        t_highestScore.setText("最高分: " + highestScore);
                        t_score.setText("分数: " + score);
                    }
                    gameOver();
                }
                if ((allBlock[snakeBody.getFirst().getNodeY()] & (int) Math.pow(2, snakeBody.getFirst().getNodeX())) != 0) {
                    gameOver();
                    sendAllblock=true;
                }
                S_eat();
                break;
            case 10:
                snakeBody.addFirst(new S_node(snakeBody.getFirst().getNodeX(), snakeBody.getFirst().getNodeY() - 1));
                if (snakeBody.getFirst().getNodeY() < 0) {
                    if (score > getHighestScore(level)) {
                        highestScore = score;
                        t_highestScore.setText("最高分: " + highestScore);
                        t_score.setText("分数: " + score);
                    }
                    gameOver();
                } else {
                    if ((allBlock[snakeBody.getFirst().getNodeY()] & (int) Math.pow(2, snakeBody.getFirst().getNodeX())) != 0) {
                        gameOver();
                        sendAllblock=true;
                    }
                }
                S_eat();
                break;
            case 11:
                snakeBody.addFirst(new S_node(snakeBody.getFirst().getNodeX(), snakeBody.getFirst().getNodeY() + 1));
                if (snakeBody.getFirst().getNodeY() > 14) {
                    if (score > getHighestScore(level)) {
                        highestScore = score;
                        t_highestScore.setText("最高分: " + highestScore);
                        t_score.setText("分数: " + score);
                    }
                    gameOver();
                } else {
                    if ((allBlock[snakeBody.getFirst().getNodeY()] & (int) Math.pow(2, snakeBody.getFirst().getNodeX())) != 0) {
                        gameOver();
                        sendAllblock=true;
                    }
                }
                S_eat();
                break;
        }
    }
    //蛇判定食物及自身函数
    void S_eat(){
        snakeBodyC = (LinkedList<S_node>) snakeBody.clone();
        snakeBodyC.remove();
        for (S_node node : snakeBodyC) {
            if (node.getNodeX() == snakeBody.getFirst().getNodeX() && node.getNodeY() == snakeBody.getFirst().getNodeY()) {
                if (score>getHighestScore(level)){
                    highestScore=score;
                    t_highestScore.setText("最高分: "+highestScore);
                    t_score.setText("分数: "+score);
                }
                gameOver();
            }
        }
        if (snakeBody.getFirst().getNodeX() == food.getNodeX() && snakeBody.getFirst().getNodeY() == food.getNodeY()) {
            score++;
            t_score.setText("分数: "+score);
            food = S_food();
        } else
            snakeBody.removeLast();
    }
    //---------------------------------------------

    //---------------------------------------------
    //食物生成函数
    S_node S_food(){
        random=new Random();
        S_node foodrandom = new S_node( random.nextInt(9), random.nextInt(14));
        return foodrandom;
    }
    //---------------------------------------------

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
                score+=10;
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
            if (score>getHighestScore(level)){
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

        nextRand=random.nextInt(20);
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
                status=0;
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
        update(highestScore,level);
        stopTimer();
        //创建弹窗
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("游戏结束");
        builder.setMessage("本局得分: "+score);
        refresh=false;
        GameOver=true;
        if(SelectMode.intnetMode==0) {
            //在弹窗中设置  再来一局  按钮
            builder.setPositiveButton("再来一局", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    score = 0;
                    t_score.setText("分数: " + score);
                    position[0] = -4;  //初始化方块位置
                    position[1] = 4;
                    for (int i = 0; i < ySize; i++) {
                        allBlock[i] = 0;
                        for (int j = 0; j < xSize; j++) {
                            b_color[i][j] = 0;
                        }
                    }
                    rand = random.nextInt(20);
                    position[0] = B_Shape.initPosition[rand][1];
                    position[1] = B_Shape.initPosition[rand][0];
                    randColor = random.nextInt(5) + 1;
                    nextRand = random.nextInt(20);
                    nextRandColor = random.nextInt(5) + 1;
//---------------------------------------------------------------
                    direction = 12;
                    snakeBody.clear();
                    for (int i = 4; i <= 6; i++)
                        snakeBody.addLast(new S_node(i, 7));
                    refresh = true;
//---------------------------------------------------------------

                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            status = 0;
                            handler.sendEmptyMessage(0);
                        }
                    }, 0, timeInterval);
                }
            });
        }

        //退出  按钮
        builder.setNegativeButton("退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mediaPlayer.pause();
                finish();
                Intent intent=new Intent(MainActivity.this,SelectMode.class);
                startActivity(intent);
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
        btn_pause=findViewById(R.id.pause);
        btn_setting=findViewById(R.id.setting);

// -----------------------------------------------------
        s_btn_down=findViewById(R.id.s_btn_down);
        s_btn_left=findViewById(R.id.s_btn_left);
        s_btn_right=findViewById(R.id.s_btn_right);
        s_btn_up=findViewById(R.id.s_btn_up);
        server=null;
        client=null;
        sendAllblock=false;
//------------------------------------------------------

        for (int i=0;i<10;i++){
            for (int j=0;j<15;j++){
                blockList.add(0);
            }
        }

        //获得方块属性并显示在画布上
        block=new B_Adapter(MainActivity.this,blockList,R.layout.layout);
        b_s_view.setAdapter(block);

        random=new Random();
        rand=random.nextInt(20);
        position[0]=B_Shape.initPosition[rand][1];
        position[1]=B_Shape.initPosition[rand][0];
        randColor=random.nextInt(5)+1;
        nextRand=random.nextInt(20);
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



        // ---------------------------------------------------------
        snakeBody = new LinkedList<>();
        for(int i=4;i<=6;i++)
            snakeBody.addLast(new S_node(i,7));
        status=0;
        direction=12;
        food=S_food();
       //-----------------------------------------------------------
        timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                status=0;
                handler.sendEmptyMessage(0);
            }
        },0,timeInterval);

        startMusic();

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
                status=1;
             //   handler.sendEmptyMessage(1);
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
                status=1;
                //handler.sendEmptyMessage(1);
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
                status=1;
               // handler.sendEmptyMessage(1);
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
                    status=0;
                   // handler.sendEmptyMessage(0);
                }
            }
        });
        //------------------------------------------------------------------------------
        //蛇上移按钮
        s_btn_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(direction!=11&&direction!=10) {
                    direction = 10;
                    handler.sendEmptyMessage(10);
                }
            }
        });
        //蛇下移按钮
        s_btn_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(direction!=10&&direction!=11) {
                    direction = 11;
                    handler.sendEmptyMessage(11);
                }
            }
        });
        s_btn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(direction!=13&&direction!=12) {
                    direction = 12;
                    handler.sendEmptyMessage(12);
                }
            }
        });
        s_btn_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(direction!=12&&direction!=13) {
                    direction = 13;
                    handler.sendEmptyMessage(13);
                }
            }
        });
//-----------------------------------------------------------------------------------
        //暂停按钮
        btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SelectMode.intnetMode==0)
                    pause();
            }
        });
        //设置按钮
        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                music=getMusic();
                if(SelectMode.intnetMode==0)
                     if (!p)
                          pause();
                     setting();

            }
        });
    }

    //选择难度
    public void changeLevel(){
        Intent intent=getIntent();
        level=intent.getIntExtra("level",2);
        switch (level){
            case 1:
                timeInterval=1000;
                break;
            case 2:
                timeInterval=800;
                break;
            case 3:
                timeInterval=600;
                break;
            case 4:
                timeInterval=400;
                break;
            case 5:
                timeInterval=1000;
                break;
        }
    }

    //数据库更新函数
    public void update(int highScore,int le){
        db=dBhelper.getWritableDatabase();
        int id=1;
        switch (le){
            case 1:
                db.execSQL("update UserInfo set easy=? where id=?",new Object[]{highScore,id});
                break;
            case 2:
                db.execSQL("update UserInfo set ordinary=? where id=?",new Object[]{highScore,id});
                break;
            case 3:
                db.execSQL("update UserInfo set hard=? where id=?",new Object[]{highScore,id});
                break;
            case 4:
                db.execSQL("update UserInfo set other=? where id=?",new Object[]{highScore,id});
                break;
            case 5:
                db.execSQL("update UserInfo set more=? where id=?",new Object[]{highScore,id});
                break;
        }
        db.close();
    }

    //数据库查询函数
    public void select(int le){
        db=dBhelper.getReadableDatabase();
        int id=1;
        Cursor cursor=db.rawQuery("select * from UserInfo where id=?",new String[]{String.valueOf(id)});
        if (cursor.getCount()!=0){
            cursor.moveToFirst();
            switch (le){
                case 1:
                    t_highestScore.setText("最高分: "+cursor.getString(cursor.getColumnIndex("easy")));
                    t_level.setText("等级: 简单");
                    break;
                case 2:
                    t_highestScore.setText("最高分: "+cursor.getString(cursor.getColumnIndex("ordinary")));
                    t_level.setText("等级: 普通");
                    break;
                case 3:
                    t_highestScore.setText("最高分: "+cursor.getString(cursor.getColumnIndex("hard")));
                    t_level.setText("等级: 困难");
                    break;
                case 4:
                    t_highestScore.setText("最高分: "+cursor.getString(cursor.getColumnIndex("other")));
                    t_level.setText("等级: 噩梦");
                    break;
                case 5:
                    t_highestScore.setText("最高分: "+cursor.getString(cursor.getColumnIndex("more")));
                    t_level.setText("等级: 双人");
                    break;
            }
        }
        db.close();
    }

    //获取最高分
    public int getHighestScore(int level){
        db=dBhelper.getReadableDatabase();
        int id=1;
        Cursor cursor=db.rawQuery("select * from UserInfo where id=?",new String[]{String.valueOf(id)});
        if (cursor.getCount()!=0){
            cursor.moveToFirst();
            switch (level){
                case 1:
                    highestScore=Integer.parseInt(cursor.getString(cursor.getColumnIndex("easy")));
                    break;
                case 2:
                    highestScore=Integer.parseInt(cursor.getString(cursor.getColumnIndex("ordinary")));
                    break;
                case 3:
                    highestScore=Integer.parseInt(cursor.getString(cursor.getColumnIndex("hard")));
                    break;
                case 4:
                    highestScore=Integer.parseInt(cursor.getString(cursor.getColumnIndex("other")));
                    break;
                case 5:
                    highestScore=Integer.parseInt(cursor.getString(cursor.getColumnIndex("more")));
                    break;
            }
        }
        db.close();
        return highestScore;
    }

    //获取音乐变量
    public int getMusic(){
        db=dBhelper.getReadableDatabase();
        int id=1;
        Cursor cursor=db.rawQuery("select * from UserInfo where id=?",new String[]{String.valueOf(id)});
        if (cursor.getCount()!=0){
            cursor.moveToFirst();
            music=Integer.parseInt(cursor.getString(cursor.getColumnIndex("music")));
        }
        db.close();
        return music;
    }

    //保存音乐变量
    public void setMusic(int music){
        db=dBhelper.getWritableDatabase();
        int id=1;
        db.execSQL("update UserInfo set music=? where id=?",new Object[]{music,id});
        db.close();
    }

    //是否按下返回键
    public void onBackPressed() {
        pause();
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

    //暂停游戏+开始游戏
    public void pause(){
        Drawable pa= ResourcesCompat.getDrawable(getResources(),R.drawable.pause,null);
        Drawable st= ResourcesCompat.getDrawable(getResources(),R.drawable.start,null);
        if (!p){
            stopTimer();
            b_btn_down.setEnabled(false);
            b_btn_up.setEnabled(false);
            b_btn_right.setEnabled(false);
            b_btn_left.setEnabled(false);
            s_btn_down.setEnabled(false);
            s_btn_left.setEnabled(false);
            s_btn_right.setEnabled(false);
            s_btn_up.setEnabled(false);
            btn_pause.setBackground(pa);
            mediaPlayer.pause();
        }
        else {
            startTimer();
            b_btn_down.setEnabled(true);
            b_btn_up.setEnabled(true);
            b_btn_right.setEnabled(true);
            b_btn_left.setEnabled(true);
            s_btn_down.setEnabled(true);
            s_btn_left.setEnabled(true);
            s_btn_right.setEnabled(true);
            s_btn_up.setEnabled(true);
            btn_pause.setBackground(st);
            mediaPlayer.start();
        }
        p=!p;
    }

    //背景音乐
    public void startMusic(){
        if (getMusic()==1)
            mediaPlayer = MediaPlayer.create(this, R.raw.music2);
        else
            mediaPlayer = MediaPlayer.create(this, R.raw.music1);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    //设置
    public void setting(){
        final setting s=new setting(MainActivity.this);
        s.setTitle("设置");
        s.show();
        s.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (s.music1=="begin"){
                    if (SelectMode.intnetMode==0)
                        pause();
                    mediaPlayer.pause();
                    setMusic(s.m);
                    startMusic();
                }
                else if (s.music1=="end"){
                    if (SelectMode.intnetMode==0)
                        pause();
                    mediaPlayer.pause();
                    setMusic(s.m);
                }
                else{
                    Intent intent=new Intent(MainActivity.this,SelectMode.class);
                    startActivity(intent);
                    mediaPlayer.pause();
                }
            }
        });
    }

    //联网设置
    public void Inetnet(){
        if(createRoom.Mode==0){
            s_btn_right.setEnabled(false);
            s_btn_down.setEnabled(false);
            s_btn_up.setEnabled(false);
            s_btn_left.setEnabled(false);
        }
        if(createRoom.Mode==1){
            b_btn_down.setEnabled(false);
            b_btn_left.setEnabled(false);
            b_btn_right.setEnabled(false);
            b_btn_up.setEnabled(false);
        }
    }

    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        if (!p)
            pause();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dBhelper=new DBhelper(this);
        changeLevel();
        init();
        if(SelectMode.intnetMode==1)
            Inetnet();
        select(level);
        btn_Move();
    }

    public Runnable ClientListener = new Runnable() {
        @Override
        public void run() {
            try {
                Socket socket = new Socket(CreateRoom.ServerIP,CreateRoom.ServerPort);
                ObjectOutputStream objectOutputStream=null;
                objectOutputStream=new ObjectOutputStream(socket.getOutputStream());
                S_snake s_snake=new S_snake();
                s_snake.setLinkedList(snakeBody,"snake",refresh);
                s_snake.setFood(food);
                if(sendAllblock){
                    sendAllblock=false;
                    s_snake.setB_color(b_color);
                     s_snake.setAllBlock(allBlock); }
                objectOutputStream.writeObject(s_snake);
                objectOutputStream.flush();

                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                B_info b_info=(B_info) objectInputStream.readObject();
                allBlock=b_info.getAllblock();
                b_color=b_info.getB_color();
                blockList=b_info.getBlockList();
                blockNextList=b_info.getBlockNextList();
                rand=b_info.getRand();
                position=b_info.getPosition();
                randColor=b_info.getRandColor();
                GameOver=b_info.getGameOver();
                socket.close();
                socket=null;
            } catch (IOException e) {
                e.printStackTrace();
            }
            catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    };

    public  Runnable ServerListener = new Runnable() {
        @Override
        public void run() {

            try {
                while(true) {
                    Socket socket = mServerSocket.accept();
                    ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                    S_snake s_snake = (S_snake) objectInputStream.readObject();
                    snakeBody=s_snake.getLinkedList();
                    food=s_snake.getFood();
                    refresh=s_snake.getrefresh();
                    int [] b=s_snake.getAllBlock();
                    for(int a:b){
                        if(a!=0){
                            b_color=s_snake.getB_color();
                            allBlock=s_snake.getAllBlock();
                        }
                    }

                    ObjectOutputStream objectOutputStream=new ObjectOutputStream(socket.getOutputStream());
                    B_info b_info=new B_info();
                    b_info.setB_info(allBlock,blockList,blockNextList,b_color,position,rand,randColor,GameOver);
                    objectOutputStream.writeObject(b_info);
                    objectOutputStream.flush();

                    if(!refresh){
                        mServerSocket=null;
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    };
}
