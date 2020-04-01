package com.netty.demo.zerocopy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class ZeroCopyClient {

    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost",7001));
        FileChannel outChannel = FileChannel.open(Paths.get(""), StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW);

        //在linux下一次调用transfer()就可以完成传输，但在windows中一次只能传输8M，需要注意传输时的位置进行分段传输；

        //底层使用到零拷贝
        long count = outChannel.transferTo(0, outChannel.size(), socketChannel);
        outChannel.close();
    }

}
