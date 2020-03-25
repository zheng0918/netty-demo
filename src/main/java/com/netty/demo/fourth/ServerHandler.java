package com.netty.demo.fourth;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;

import java.util.UUID;

/**
 * @Author Zhengx
 * @Date 2019/9/12
 * @Description
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {


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
