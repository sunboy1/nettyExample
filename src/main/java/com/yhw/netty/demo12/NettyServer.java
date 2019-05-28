package com.yhw.netty.demo12;

import com.yhw.netty.codec.PacketCodeHandler;
import com.yhw.netty.codec.Spliter;
import com.yhw.netty.demo12.server.AuthHandler;
import com.yhw.netty.demo12.server.IMHandler;
import com.yhw.netty.demo12.server.LoginRequestHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 优化：使用单例模式共享handler 添加handler(不需要每次新连接都 新建 handler,都是无状态[没有成员变量])
 * @ChannelHandler.Sharable  非常重要：加上注解标识，显示告诉netty 表明该 handler 是可以多个 channel 共享的
 */
public class NettyServer {
    private ServerBootstrap bootstrap;

    {
        bootstrap = new ServerBootstrap();
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup work = new NioEventLoopGroup();

        bootstrap.group(boss,work)
                .channel(NioServerSocketChannel.class)
                //表示系统用于临时存放已完成三次握手的请求队列的最大长度
                //如果连接建立频繁
                //服务器处理创建新连接较慢，可以适当调大这个参数
                .option(ChannelOption.SO_BACKLOG,1024)
                .childOption(ChannelOption.SO_KEEPALIVE,true)
                .childOption(ChannelOption.TCP_NODELAY,true);
    }

    public NettyServer(){
        bootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
                //需要维护每个channel 当前读取到的数据，是有状态的
                ch.pipeline().addLast(new Spliter());
                ch.pipeline().addLast(PacketCodeHandler.INSTANCE);
                ch.pipeline().addLast(LoginRequestHandler.INSTANCE);
                ch.pipeline().addLast(AuthHandler.INSTANCE);
                //压索handler,通过具体的指令找到具体的 handler
                ch.pipeline().addLast(IMHandler.INSTANCE);

            }
        })
                .bind(8800)
                .addListener(future -> {
                    if(future.isSuccess()){
                        System.out.println("端口绑定成功");
                    }else {
                        System.out.println("端口绑定失败");
                    }
                });
    }

    public static void main(String[] args){
         NettyServer server = new NettyServer();
    }
}
