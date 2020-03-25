package com.netty.demo.second;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.time.LocalDateTime;

/**
 * @Author Zhengx
 * @Date 2019/9/12
 * @Description
 */
public class MyClientHandler extends SimpleChannelInboundHandler<String> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg){
        System.out.println(ctx.channel().remoteAddress());
        System.out.println("first output :" + msg);
        ctx.writeAndFlush(" from first : " + LocalDateTime.now());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(" 客户端说:现在是" + LocalDateTime.now());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
