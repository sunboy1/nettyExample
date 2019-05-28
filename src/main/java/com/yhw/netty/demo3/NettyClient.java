package com.yhw.netty.demo3;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 测试客户端登录
 */
public class NettyClient {

    private Bootstrap bootstrap;
    {
        bootstrap = new Bootstrap();

        NioEventLoopGroup grop = new NioEventLoopGroup();

        bootstrap.group(grop)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new ClientHandler());
                    }
                });
    }

    public void connect(String host,int port){
        bootstrap.connect(host,port).addListener(future -> {
            if(future.isSuccess()){
                System.out.println("连接成功");
            }else{
                System.out.println("连接失败");
            }
        });
    }

    public static void main(String[] args){
         NettyClient nettyClient = new NettyClient();
         nettyClient.connect("localhost",8800);
    }
}
