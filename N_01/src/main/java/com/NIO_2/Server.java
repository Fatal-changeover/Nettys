package com.NIO;


import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

import static com.utils.ByteBufferUtil.debugRead;
import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

/**
 * ClassName: Server
 * Package: com.NIO
 * Description:
 *
 * @Author R
 * @Create 2024/5/30 10:54
 * @Version 1.0
 */
@Slf4j
public class Server {
    public static void main(String[] args) throws IOException {
        //使用nio理解阻塞模式
        //0.ByteBuffer
        ByteBuffer buffer = ByteBuffer.allocate(16);
        //1.创建服务器
        ServerSocketChannel ssc = ServerSocketChannel.open();
        //2.绑定端口
        ssc.bind(new InetSocketAddress(8080));
        //3.连接集合
        ArrayList<SocketChannel> channels = new ArrayList<>();
        while (true) {
            //4.accept 建立与客户端得连接 socketchannel 用来与客户端之间进行通信
            log.debug("connecting...");
            SocketChannel sc = ssc.accept();
            log.debug("connected! {}",sc);
            channels.add(sc);
            for (SocketChannel channel : channels) {
                log.debug("before read... {}",sc);
                //5.接收客户端发送的数据
                channel.read(buffer);
                //切换读模式
                buffer.flip();
                debugRead(buffer);
                buffer.clear();
                log.debug("after read...{}",sc);
            }
        }
    }
}
