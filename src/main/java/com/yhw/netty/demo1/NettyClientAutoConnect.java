package com.yhw.netty.demo1;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.TimeUnit;

/**
 * netty客户端自动重连
 */
public class NettyClientAutoConnect {

    private Bootstrap bootstrap;

    private int MAX_RETRY = 5;

    public NettyClientAutoConnect(){
        bootstrap = new Bootstrap();

        NioEventLoopGroup grop = new NioEventLoopGroup();

        bootstrap.group(grop)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {

                    }
                });
    }

    /**
     *
     * @param host host
     * @param port 端口
     * @param retry 重连次数
     */
    public void reConnect(String host,int port,int retry){
        bootstrap.connect(host,port).addListener(future->{
            if(future.isSuccess()){
                System.out.println("连接成功");
            }else if(retry <= 0){
                System.out.println("重试次数已用完，放弃连接");
            }else{
                //第几次重连
                int order = (MAX_RETRY - retry) + 1;
                //本地重连时间间隔
                int delay = 1 << order;
                System.out.println("连接失败，第"+order+"次重连");
                //实现线程组，定时重新连接
                bootstrap.config()
                        .group()
                        .schedule(()->{reConnect(host,port+1,retry-1);},delay, TimeUnit.SECONDS);
            }
        });
    }

    public void option(){
        //连接超时时间
        bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS,5000)
                .option(ChannelOption.SO_KEEPALIVE,true)//心跳
                //nagle算法，要求高实时性,有数据马上发送，就设置成true,
                //如果需要减少发送的次数，减少网络交换，设置成false
                .option(ChannelOption.TCP_NODELAY,true);
    }


    public static void main(String[] args){
         NettyClientAutoConnect clientAutoConnect = new NettyClientAutoConnect();
         clientAutoConnect.reConnect("localhost",8079,5);
    }


}
