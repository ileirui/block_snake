package com.example.block_snake;

import java.net.InetAddress;

public class Addr {
    String serverip,clientip;
    int serverport,clientport;
    public Addr(){
        serverip=null;
        clientip=null;
        serverport=0;
        clientport=0;
    }
    public void setport(int server,int client){
        serverport=server;
        clientport=client;
    }
    public void setip(String server ,String client){
        serverip=server;
        clientip=client;
    }
    public String getServerip(){
        return serverip;
    }
    public String getClientip(){
        return clientip;
    }
    public int getServerport(){
        return serverport;
    }
    public int getClientport(){
        return clientport;
    }
}
