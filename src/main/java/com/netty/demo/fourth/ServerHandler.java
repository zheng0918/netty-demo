package com.netty.demo.fourth;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;

import java.nio.charset.CharsetEncoder;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author Zhengx
 * @Date 2019/9/12
 * @Description
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 用户自定义任务
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //自定义任务
        ctx.channel().eventLoop().execute(() -> {
            try{
                Thread.sleep(5*1000);
                ctx.writeAndFlush(Unpooled.copiedBuffer("hello", CharsetUtil.UTF_8));
            }catch (Exception e){
                System.out.println("发生异常"+e.getMessage());
            }
        });
        //自定义定时任务
        ctx.channel().eventLoop().schedule(() -> {
            try{
                Thread.sleep(5*1000);
                ctx.writeAndFlush(Unpooled.copiedBuffer("hello", CharsetUtil.UTF_8));
            }catch (Exception e){
                System.out.println("发生异常"+e.getMessage());
            }
        },5, TimeUnit.SECONDS);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt){
        if(evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent) evt;
            String eventType = null;
            switch (event.state()){
                case ALL_IDLE:
                    eventType = "读写空闲";
                    break;
                case READER_IDLE:
                    eventType = "读空闲";
                    break;
                case WRITER_IDLE:
                    eventType = "写空闲";
                    break;
            }
            System.out.println(ctx.channel().remoteAddress() + " 超市时间:"+ eventType);
            ctx.channel().close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        cause.printStackTrace();
        ctx.close();
    }
}
