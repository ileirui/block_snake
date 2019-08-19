package com.example.block_snake;

import android.content.Context;

import java.io.Serializable;
import java.util.List;

public class B_info implements Serializable {
    private static final long serialVersionUID= -34975938475934933L;
    int[] allblock;
    int[][] b_color=new int[15][10];
    List<Integer> blockList;
    List<Integer> blockNextList;
    public String name;
    int[] position=new int[]{-4,4};
    int rand;
    int randColor;
    boolean Server_stop=false;
    boolean Gameover=false;



    public void setB_info(int[] allblock,List<Integer> blockList,List<Integer> blockNextList,int[][]b_color,int[]position,int rand,int randColor,boolean stop,boolean GameOver){
        this.allblock=allblock;
        this.blockList=blockList;
        this.blockNextList=blockNextList;
        this.b_color=b_color;
        this.position=position;
        this.rand=rand;
        this.randColor=randColor;
        this.Server_stop=stop;
        this.Gameover=GameOver;
    }

    public int[] getAllblock(){
        return allblock;
    }
    public List<Integer> getBlockList(){
        return blockList;
    }
    public List<Integer> getBlockNextList(){
        return blockNextList;
   }
    public void setName(String string) {
        name=string;
    }
    public String getName(){
        return name;
    }
    public int[][] getB_color(){ return b_color; }
    public int[] getPosition(){ return position; }
    public int getRand(){ return rand; }
    public int getRandColor(){ return randColor;}
    public boolean getServerstop(){return Server_stop;}
    public boolean getGameOver(){return Gameover;}
}
