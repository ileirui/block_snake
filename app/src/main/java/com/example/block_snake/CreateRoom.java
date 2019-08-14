package com.example.block_snake;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import static java.lang.Thread.sleep;

public class CreateRoom extends AppCompatActivity {
    Button btn_create,btn_back,btn_join;
    TextView user_name;
    ImageView btn_picture;
    UserInformation u=new UserInformation();

    public static int Mode = 1;

    public static ServerSocket mServerSocket;
    int mLocalPort=0;
    NsdManager nsdManager;
    NsdServiceInfo nsdServiceInfo;
    NsdManager.DiscoveryListener nsDicListener;
    NsdManager.RegistrationListener nsRegListener;
    NsdManager.ResolveListener nsResolveListener;

    public Thread waitclient=null;
    public Thread change=null;
    public static InetAddress ServerIP;
    public static int ServerPort;
    int c=0,destory=0;
    boolean canrun=true;
    S_snake s_snake=new S_snake();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room);
        btn_back=findViewById(R.id.btn_back);
        btn_create=findViewById(R.id.btn_create);
        btn_join=findViewById(R.id.btn_join);
        user_name=findViewById(R.id.user_name);
        btn_picture=findViewById(R.id.user_picture);
        user_name.setText(u.getName(CreateRoom.this));
        btn_picture.setImageBitmap(u.getBitmip());

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mServerSocket = new ServerSocket(0);
                    mLocalPort = mServerSocket.getLocalPort();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                canrun=true;
                Mode=0;
                nsdServiceInfo = new NsdServiceInfo();
                nsdServiceInfo.setServiceName("NSD_Test_Program");
                nsdServiceInfo.setServiceType("_http._tcp.");
                nsdServiceInfo.setPort(mLocalPort);
                initializeServerSocket();
                change = new Thread(ServerListener);
                change.start();
                waitclient = new Thread(WaitClient);
                waitclient.start();
            }
        });

        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mServerSocket = new ServerSocket(0);
                    mLocalPort = mServerSocket.getLocalPort();
                    mServerSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Mode=1;
                // 注册网络服务的名称、类型、端口
                nsdServiceInfo = new NsdServiceInfo();
                nsdServiceInfo.setServiceName("NSD_Test_Program");
                nsdServiceInfo.setServiceType("_http._tcp.");
                nsdServiceInfo.setPort(mLocalPort);
                discoverService();
                initResolveListener();
            }
        });



        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unregisterService();
                canrun=false;
                Intent intent=new Intent(CreateRoom.this,SelectMode.class);
                startActivity(intent);
            }
        });
        btn_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CreateRoom.this,UserInformation.class);
                startActivity(intent);
            }
        });
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==0){
                c++;
                if(c%10==0)
                    btn_create.setText(String.valueOf(c/10));
            }
            else if(msg.what==1){
                Intent intent=new Intent(CreateRoom.this,MainActivity.class);
                intent.putExtra("level",1);
                startActivity(intent);
            }
        }
    };

    public Runnable WaitClient = new Runnable() {
        @Override
        public void run() {
            while (canrun) {
                destory=0;
                while (!s_snake.getName().equals("snake")) {
                    destory++;
                    try {
                        Message message = Message.obtain();
                        message.what = 0;
                        Thread.sleep(100);
                        handler.sendMessage(message);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(destory==10)
                        break;
                }
                if(s_snake.getName().equals("snake")){
                Message message1 = Message.obtain();
                message1.what = 1;
                handler.sendMessage(message1);
                break;
                }
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
                    s_snake = (S_snake) objectInputStream.readObject();
                    if(s_snake.getName().equals("snake")){
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
    public void onBackPressed() {
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

    public void initializeServerSocket() {
        // Initialize a server socket on the next available port.

        // 实现一个网络服务的注册事件监听器，监听器的对象应该保存起来以便之后进行注销
        nsRegListener = new NsdManager.RegistrationListener() {
            @Override
            public void onUnregistrationFailed(NsdServiceInfo arg0, int arg1) {
                Toast.makeText(getApplicationContext(), "Unregistration Failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onServiceUnregistered(NsdServiceInfo arg0) {
                Toast.makeText(getApplicationContext(), "Service Unregistered", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onServiceRegistered(NsdServiceInfo arg0) {
                Toast.makeText(getApplicationContext(), "Service Registered", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRegistrationFailed(NsdServiceInfo arg0, int arg1) {
                Toast.makeText(getApplicationContext(), "Registration Failed", Toast.LENGTH_SHORT).show();
            }
        };
        // 获取系统网络服务管理器，准备之后进行注册
        nsdManager = (NsdManager) getApplicationContext().getSystemService(Context.NSD_SERVICE);
        nsdManager.registerService(nsdServiceInfo, NsdManager.PROTOCOL_DNS_SD, nsRegListener);
    }

    public void discoverService() {
        nsDicListener = new NsdManager.DiscoveryListener() {
            @Override
            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                Toast.makeText(getApplicationContext(), "Stop Discovery Failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                Toast.makeText(getApplicationContext(),
                        "Start Discovery Failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onServiceLost(NsdServiceInfo serviceInfo) {
                Toast.makeText(getApplicationContext(), "Service Lost", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onServiceFound(NsdServiceInfo serviceInfo) {
                // 发现网络服务时就会触发该事件
                // 可以通过switch或if获取那些你真正关心的服务
                Toast.makeText(getApplicationContext(), "Service Found", Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(),serviceInfo.getServiceName(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDiscoveryStopped(String serviceType) {
                Toast.makeText(getApplicationContext(), "Discovery Stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDiscoveryStarted(String serviceType) {
                Toast.makeText(getApplicationContext(), "Discovery Started", Toast.LENGTH_SHORT).show();
            }
        };
        nsdManager = (NsdManager) getApplicationContext().getSystemService(Context.NSD_SERVICE);
        nsdManager.discoverServices("_http._tcp", NsdManager.PROTOCOL_DNS_SD,nsDicListener);
    }

    Handler handler_client=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ServerIP=null;
            NsdServiceInfo info;
            info=(NsdServiceInfo) msg.obj;
            ServerPort=info.getPort();
            ServerIP=info.getHost();
            if(ServerIP!=null){
                Intent intent=new Intent(CreateRoom.this,MainActivity.class);
                intent.putExtra("level",1);
                startActivity(intent);
           }
        }
    };
    public void initResolveListener() {
        nsResolveListener = new NsdManager.ResolveListener() {
            @Override
            public void onServiceResolved(NsdServiceInfo arg0) {
                Message message=Message.obtain();
                message.obj=arg0;
                handler_client.sendMessage(message);
                // 可以再这里获取相应网络服务的地址及端口信息，然后决定是否要与之建立连接。
                // 之后就是一些socket操作了
            }

            @Override
            public void onResolveFailed(NsdServiceInfo arg0, int arg1) {
            }
        };
        nsdManager.resolveService(nsdServiceInfo,nsResolveListener);
    }

    public void unregisterService() {
        nsdManager = (NsdManager) getApplicationContext().getSystemService(Context.NSD_SERVICE);
        //nsdManager.stopServiceDiscovery(nsDicListener); // 关闭网络发现
        nsdManager.unregisterService(nsRegListener);    // 注销网络服务
    }

    public String intToip(int i){
        return (i&0xFF)+"."+((i>>8)&0xFF)+"."+((i>>16)&0xFF)+"."+((i>>24)&0xFF);
    }
}
