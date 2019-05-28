package com.yhw.netty.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * 阻塞式IO服务器
 */
public class IOServer extends Thread{

    private ExecutorService executorService = Executors.newFixedThreadPool(8, new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r,"IO blockThread"+r.hashCode());
        }
    });

    //接收连接线程
    private void ServerStart() {
        try{
            //创建一个Socker通道，绑定8000端口
            ServerSocket serverSocket = new ServerSocket(8000);

            //1.接收新链接线程
            while (true){
                //1.1.阻塞方法获取新的连接
                Socket socket = serverSocket.accept();

                //1.2.每一个新的连接都创建一个线程(读取数据线程)
                executorService.submit(()->{
                    try{
                        int len;
                        byte[] data = new byte[1024];
                        InputStream inputStream = socket.getInputStream();
                        //1.3.按字节流的方式读取数据
                        while ((len = inputStream.read(data))!=-1){
                            System.out.println(Thread.currentThread().getName());
                            System.out.println(new String(data,0,len));
                        }
                    }catch (IOException e){

                    }
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void run(){
        ServerStart();
    }

    public static void main(String[] args){
        IOServer ioServer = new IOServer();
        ioServer.start();
    }
}
