package com.yhw.netty.demo1;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

/**
 * 自动绑定端口(启动流程分析)
 * 启动服务端，必须指定三类属性
 * 1.线程模型 2.IO模型 3.连接读写处理逻辑
 */
public class NettyServerAutoBind {

    ServerBootstrap serverBootstrap = new ServerBootstrap();

    public NettyServerAutoBind(){
        start();
    }

    public void start(){
        //对应IOServer中接收 新连接线程，主要负责创建新连接
        NioEventLoopGroup boss = new NioEventLoopGroup();
        //对应IOServer中负责读取数据的线程，主要用于读取数据以及业务逻辑处理
        NioEventLoopGroup worker = new NioEventLoopGroup();

        serverBootstrap
                //绑定线程组
                .group(boss,worker)
                //指定服务的IO模型
                .channel(NioServerSocketChannel.class)
                //处理每条连接的数据读写，业务处理逻辑
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    //NioSocketChannel是对NioServerSocketChannel连接的抽象
                    //可以BIO中的ServerSocket和Socket的概念对应上
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new StringDecoder());
                        ch.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
                                System.out.println(msg);
                            }
                        });
                    }
                });
    }

    /**
     * 绑定端口是最后一步，即启动
     * 通过futrue可以看出bind方法是异步的，所以可以通过异步机制来实现端口递增绑定
     * @param port
     */
    public void autoBind(int port){
        serverBootstrap.bind(port).addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                if(future.isSuccess()){
                    System.out.println("端口["+port+"]绑定成功");
                }else {
                    System.out.println("端口["+port+"]绑定失败");
                    autoBind(port+1);
                }
            }
        });
    }

    //childHandler()用于指定处理新连接数据的读写处理逻辑
    //handler() 用于指定在服务端启动过程中的一些逻辑，
    // 通常下用不到
    public void handler(){
        serverBootstrap.handler(new ChannelInitializer<NioServerSocketChannel>() {
            @Override
            protected void initChannel(NioServerSocketChannel nioServerSocketChannel) throws Exception {
                System.out.println("服务启动中");
            }
        });
    }

    //可以给服务端的channel,也就是NioServerSocketChannel指定一些自定义属性
    //然后我们可以通到 channel.attr()取出这个属性
    //就是给NioServerSocketChannel维护一个Map,通常用不到
    public void attr(){
        serverBootstrap.attr(AttributeKey.newInstance("serverName"),"nettyServer");
    }

    //给每一条连接指定自定义属性，可以通过channel.attr()取出该属性
    public void childAttr(){
        serverBootstrap.childAttr(AttributeKey.newInstance("clientKey"),"clientValue");
    }

    /**
     * 可以给每一条连接设置一些TCP连接底层相关的属性
     */
    public void childOption(){
        serverBootstrap
                //TCP是否开启底层心跳机制，true为开启
                .childOption(ChannelOption.SO_KEEPALIVE,true)
                //是否开启Nagle算法  true表示关闭  要求高实时性，有数据时马上发送，就关闭
                //如果需要减少发送次数减少网络交互，就开启
                .childOption(ChannelOption.TCP_NODELAY,true);
    }

    /**
     * 给服务端channel设置一些属性，最常见的就是SO_BACKLOG
     */
    public void option(){
        //表示系统用于临时存放已完成三次握手的请求队列的最大长度
        //如果连接建立频繁
        //服务器处理创建新连接较慢，可以适当调大这个参数
        serverBootstrap.option(ChannelOption.SO_BACKLOG,1024);
    }

    public static void main(String[] args){
        NettyServerAutoBind nettyServerAutoBind = new NettyServerAutoBind();
        nettyServerAutoBind.autoBind(8081);
    }
}
