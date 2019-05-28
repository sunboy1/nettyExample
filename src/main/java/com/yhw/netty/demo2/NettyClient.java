package com.yhw.netty.demo2;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 实现服务器与客户端双向通信
 */
public class NettyClient {

    private Bootstrap bootstrap = new Bootstrap();

    {
        //线程组
        NioEventLoopGroup grop = new NioEventLoopGroup();
        bootstrap.group(grop)
                .channel(NioSocketChannel.class);
    }

    //绑定处理器
    public void handler(){
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(new FirstClientHandler());
            }
        });
    }

    public void connect(String host,int port){
        bootstrap.connect(host,port)
                .addListener(future -> {
                   if(future.isSuccess()){
                       System.out.println("连接成功");
                   }else {
                       System.out.println("连接失败");
                   }

                });
    }

    public static void main(String[] args){
        NettyClient nettyClient = new NettyClient();
        nettyClient.handler();
        nettyClient.connect("localhost",8081);
    }

}
