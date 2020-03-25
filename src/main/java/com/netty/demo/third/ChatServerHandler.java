package com.netty.demo.third;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @Author Zhengx
 * @Date 2019/9/16
 * @Description
 */
public class ChatServerHandler extends SimpleChannelInboundHandler<String> {

    /**定义一个channel组，每次客户端与服务器建立连接时会将当前的channel添加到channelGroup中**/
    private static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();
        channels.forEach(ch -> {
            if(ch != channel){
                ch.writeAndFlush(ch.remoteAddress() + "发送的消息: " + msg);
            }else{
                ch.writeAndFlush("[自己]:" + msg);
            }
        });
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx){
        Channel channel = ctx.channel();
        //channels.writeAndFlush 表示会将channels中的所有channel遍历,并且每一个channel调用当前这个writeAndFlush方法
        channels.writeAndFlush("[服务器] - " + channel.remoteAddress() + " 加入 \n");
        channels.add(channel);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx){
        Channel channel = ctx.channel();
        channels.writeAndFlush("[服务器] - " + channel.remoteAddress() + " 离开 \n");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx){
        System.out.println(ctx.channel().remoteAddress() + " 上线 \n");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx){
        System.out.println(ctx.channel().remoteAddress() + " 下线 \n");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        cause.printStackTrace();
        ctx.close();
    }
}
