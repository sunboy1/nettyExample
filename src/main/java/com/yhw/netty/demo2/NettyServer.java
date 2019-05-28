package com.yhw.netty.demo2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 实现服务器与客户端双向通信
 */
public class NettyServer {

    private ServerBootstrap bootstrap = new ServerBootstrap();

    {
        //建立新连接线程组
        NioEventLoopGroup boss = new NioEventLoopGroup();
        //处理读写请求的线程组
        NioEventLoopGroup work = new NioEventLoopGroup();

        bootstrap
                .group(boss,work)
                //指定Io模型
                .channel(NioServerSocketChannel.class);
/*                //处理每条连接的读写，业务处理逻辑
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {

                    }
                });*/
    }

    //处理每条连接的读写，业务处理逻辑
    public void childHandler(){
        bootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                nioSocketChannel.pipeline().addLast(new FirstServerHandler());
            }
        });
    }

    public void bind(int port){
        bootstrap.bind(port).addListener(future -> {
            if(future.isSuccess()){
                System.out.println("端口绑定成功");
            }else {
                System.out.println("端口绑定失败");
            }
        });
    }

    public static void main(String[] args){
        NettyServer nettyServer = new NettyServer();
        nettyServer.childHandler();
        nettyServer.bind(8081);
    }


}
