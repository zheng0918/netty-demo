package com.netty.demo.nio;


import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * 传输较大文件，使用channel
 */
public class DataTransferChannel {

    /**
     * 使用直接缓冲区完成文件的复制
     */
    public void transferData(){
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
            inChannel = FileChannel.open(Paths.get("2.mp4"), StandardOpenOption.READ);
             outChannel = FileChannel.open(Paths.get("4.mp4"),StandardOpenOption.WRITE,
                    StandardOpenOption.READ,StandardOpenOption.CREATE_NEW);
            //create_new：如果不存在则创建，如果存在则报错； create：无论是否存在都创建；
            //内存映射文件
            MappedByteBuffer inMappedBuf  = inChannel.map(FileChannel.MapMode.READ_ONLY,0,inChannel.size());
            MappedByteBuffer outMappedBuf = outChannel.map(FileChannel.MapMode.READ_WRITE,0,inChannel.size());
            //直接对缓冲区进行数据的读写操作
            byte[] dst = new byte[inMappedBuf.limit()];
            inMappedBuf.get(dst);
            outMappedBuf.put(dst);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                outChannel.close();
                inChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
