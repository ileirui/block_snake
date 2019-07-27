package com.example.block_snake;

public class S_node {
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
