package com.netty.demo.second;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

/**
 * @Author Zhengx
 * @Date 2019/9/12
 * @Description
 */
public class MyClient {

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch){
                    ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4,0,4));
                    ch.pipeline().addLast(new LengthFieldPrepender(4));
                    ch.pipeline().addLast(new StringDecoder(CharsetUtil.UTF_8));
                    ch.pipeline().addLast(new StringEncoder(CharsetUtil.UTF_8));
                    ch.pipeline().addLast(new MyClientHandler());
                }
            });
            ChannelFuture future = bootstrap.connect("localhost", 8899).sync();
            future.channel().closeFuture().sync();
        }finally {
            eventLoopGroup.shutdownGracefully();
        }
    }
}
