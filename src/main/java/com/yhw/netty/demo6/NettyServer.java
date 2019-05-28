package com.yhw.netty.demo6;

import com.yhw.netty.codec.PacketDecoder;
import com.yhw.netty.codec.PacketEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 客户端服务器双向通信 pipeline
 */
public class NettyServer {

    private ServerBootstrap bootstrapServer;

    {
        bootstrapServer = new ServerBootstrap();
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup work = new NioEventLoopGroup();

        bootstrapServer.group(boss,work)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG,1024)
                .childOption(ChannelOption.SO_KEEPALIVE,true)
                .childOption(ChannelOption.TCP_NODELAY,true);
    }

    public void childHandler(){
        bootstrapServer.childHandler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(new PacketDecoder());
                socketChannel.pipeline().addLast(new LoginRequestHandler());
                socketChannel.pipeline().addLast(new MessageRequestHandler());
                socketChannel.pipeline().addLast(new PacketEncoder());
            }
        });
    }

    public void bind(){
        bootstrapServer.bind(8800).addListener(future -> {
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
         nettyServer.bind();
    }

}
