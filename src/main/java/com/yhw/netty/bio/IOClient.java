package com.yhw.netty.bio;

import java.net.Socket;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 阻塞式IO客户端
 */
public class IOClient {
    public static void main(String[] args){
         new Thread(()->{
             try{
                 Socket socket = new Socket("127.0.0.1",8000);
                 while (true){
                     socket.getOutputStream().write((new Date() + ": hello world").getBytes());
                     TimeUnit.SECONDS.sleep(2);
                 }
             }catch (Exception e){

             }
         }).start();
    }
}
