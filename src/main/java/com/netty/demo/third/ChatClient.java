package com.netty.demo.third;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
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

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @Author Zhengx
 * @Date 2019/9/12
 * @Description 实现聊天上线下线
 */
public class ChatClient {

    public static void main(String[] args) throws Exception {
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
                    ch.pipeline().addLast(new ChatClientHandler());//最后添加handler
                }
            });
            Channel channel = bootstrap.connect("localhost", 8899).sync().channel();
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            for(;;){
                //每次读取一行控制台的输入
                channel.writeAndFlush(br.readLine() + "/r/n");
            }
        }finally {
            eventLoopGroup.shutdownGracefully();
        }
    }
}
