package com.example.block_snake;

import java.io.Serializable;
import java.util.LinkedList;

public class S_snake implements Serializable {
    private static final long serialVersionUID= -34975938475934934L;
    private String name="default";
    public S_node food;
    public LinkedList<S_node>list;
    public void setLinkedList(LinkedList<S_node> x,String y){
        name=y;
        list=x;
    }

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

}
