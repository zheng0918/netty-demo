package com.netty.demo.fifth;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.net.InetSocketAddress;


/**
 * @Author Zhengx
 * @Date 2019/9/17
 * @Description netty对webSocket的支持
 */
public class Server {

    public static void main(String[] args) throws Exception {
         EventLoopGroup bossGroup = new NioEventLoopGroup();
         EventLoopGroup workerGroup = new NioEventLoopGroup();
         try {
             ServerBootstrap bootstrap = new ServerBootstrap();
             bootstrap.group(bossGroup,workerGroup).channel(NioServerSocketChannel.class)
             .handler(new LoggingHandler(LogLevel.INFO))
             .childHandler(new ChannelInitializer<SocketChannel>() {
                 @Override
                 protected void initChannel(SocketChannel ch){
                 ch.pipeline().addLast(new HttpServerCodec());
                 ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
                 ch.pipeline().addLast(new ChunkedWriteHandler());//以块儿的方式写
                 ch.pipeline().addLast(new HttpObjectAggregator(8192));
                 ch.pipeline().addLast(new WebSocketServerProtocolHandler("/ws"));
                 ch.pipeline().addLast(new TextWebSocketFrameHandler());
                 }
             });
             ChannelFuture future = bootstrap.bind(new InetSocketAddress(8899)).sync();
             future.channel().closeFuture().sync();
         }finally {
             bossGroup.shutdownGracefully();
             workerGroup.shutdownGracefully();
         }
    }
}
