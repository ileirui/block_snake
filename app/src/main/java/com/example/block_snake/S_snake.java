package com.example.block_snake;

import java.io.Serializable;
import java.util.LinkedList;

public class S_snake implements Serializable {
    private static final long serialVersionUID= -34975938475934934L;
    private String name="default";
    public S_node food;
    public LinkedList<S_node>list;
    int [] allBlock  = new int[15];
    int[][] b_color=new int[15][10];
    boolean refresh;
    boolean GameOver;
    public void setLinkedList(LinkedList<S_node> x,String y,boolean refresh){
        name=y;
        list=x;
        this.refresh=refresh;
    }
    public boolean getGameOver(){return GameOver;}
    public boolean getrefresh(){return refresh;}
    public LinkedList<S_node> getLinkedList(){
        return list;
    }

    public void setFood(S_node  z){
        food=z;
    }

    public S_node getFood(){
        return food;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name=name;
    }

    public void setAllBlock(int [] allBlock){
        this.allBlock=allBlock;
    }

    public int[] getAllBlock(){
        return allBlock;
    }
    public void setB_color(int [][]color){
        b_color=color;
    }
    public int[][]getB_color(){
        return b_color;
    }
}
