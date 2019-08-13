package com.example.block_snake;

import java.io.Serializable;

public class S_node implements Serializable {
    private static final long serialVersionUID= -34975938475934935L;
    private int nodeX;
    private int nodeY;

    public S_node(int nodeX, int nodeY) {
        this.nodeX = nodeX;
        this.nodeY = nodeY;
    }

    public void setXY(int X,int Y){
        this.nodeX=X;
        this.nodeY=Y;
    }
    public int getNodeX() {
        return nodeX;
    }
    public int getNodeY() {
        return nodeY;
    }

}
