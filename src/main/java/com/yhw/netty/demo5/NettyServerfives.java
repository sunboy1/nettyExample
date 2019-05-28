package com.yhw.netty.demo5;

import com.yhw.netty.demo5.inbound.InBoundHandlerA;
import com.yhw.netty.demo5.inbound.InBoundHandlerB;
import com.yhw.netty.demo5.inbound.InBoundHandlerC;
import com.yhw.netty.demo5.outbound.OutBoundHandlerA;
import com.yhw.netty.demo5.outbound.OutBoundHandlerB;
import com.yhw.netty.demo5.outbound.OutBoundHandlerC;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * netty pipeline
 * ChannelInboundHandler 的事件传播   FIFO
 * ChannelOutboundHandler 的事件传播  LIFO
 */
public class NettyServerfives {

    private ServerBootstrap bootstrapServer;

    {
        bootstrapServer = new ServerBootstrap();
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup work = new NioEventLoopGroup();

        bootstrapServer.group(boss,work)
                .channel(NioServerSocketChannel.class);
    }

    public void childHandler(){
        bootstrapServer.childHandler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel socketChannel) throws Exception {
//                socketChannel.pipeline().addLast(new ServerHandlerFour());

                socketChannel.pipeline().addLast(new InBoundHandlerA());
                socketChannel.pipeline().addLast(new InBoundHandlerB());
                socketChannel.pipeline().addLast(new InBoundHandlerC());

                socketChannel.pipeline().addLast(new OutBoundHandlerA());
                socketChannel.pipeline().addLast(new OutBoundHandlerB());
                socketChannel.pipeline().addLast(new OutBoundHandlerC());

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
        NettyServerfives nettyServer = new NettyServerfives();
        nettyServer.childHandler();
        nettyServer.bind();
    }
}
