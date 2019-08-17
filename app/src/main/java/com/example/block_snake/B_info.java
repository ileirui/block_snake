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


    public void setB_info(int[] allblock,List<Integer> blockList,List<Integer> blockNextList,int[][]b_color){
        this.allblock=allblock;
        this.blockList=blockList;
        this.blockNextList=blockNextList;
        this.b_color=b_color;
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
    public int[][] getB_color(){
        return b_color;
    }

}
