package com.NIO_6;


import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;

import static com.utils.ByteBufferUtil.debugAll;
import static com.utils.ByteBufferUtil.debugRead;

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
    private static void split(ByteBuffer source) {
        log.debug("split事件");
        source.flip();
        for (int i = 0; i < source.limit(); i++) {
            // 找到一条完整消息
            if (source.get(i) == '\n') {
                int length = i + 1 - source.position();
                // 把这条完整消息存入新的 ByteBuffer
                ByteBuffer target = ByteBuffer.allocate(length);
                // 从 source 读，向 target 写
                for (int j = 0; j < length; j++) {
                    target.put(source.get());
                }
                debugAll(target);
            }
        }
        source.compact();
    }
    public static void main(String[] args) throws IOException {

        //1.创建selector，管理多个channel
        Selector selector = Selector.open();
        ByteBuffer buffer = ByteBuffer.allocate(16);
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        /**
         * accept 有链接请求时触发
         * connect 客户端 连接建立后触发
         * read 可读事件
         * write 可写事件
         */
        SelectionKey sscKey = ssc.register(selector, 0, null);
        sscKey.interestOps(SelectionKey.OP_ACCEPT);
        log.debug("注册事件{}",sscKey);
        ssc.bind(new InetSocketAddress(8080));
        ArrayList<SocketChannel> channels = new ArrayList<>();
        while (true) {
            //3.select方法  无事件就会阻塞 有事件会恢复运行  select在事件未处理不会阻塞
            selector.select();
            Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                //处理key要在selectedkeys删除 否则不会在集合失效
                iter.remove();
                log.debug("发生事件{}",key);
                //区分事件类型
                if (key.isAcceptable()) { //如果是accept
                    ServerSocketChannel sscChannel = (ServerSocketChannel) key.channel();
                    SocketChannel sc = sscChannel.accept();
                    //设置非阻塞 将sc注册到selector上
                    sc.configureBlocking(false);
                    SelectionKey scKey = sc.register(selector, 0, null);
                    scKey.interestOps(SelectionKey.OP_READ);
                    log.debug("建立连接{}",sc);
                }else if(key.isReadable()) {
                   try {
                       SocketChannel channel = (SocketChannel) key.channel();
                       ByteBuffer buffer1 = ByteBuffer.allocate(16);
                       int read = channel.read(buffer1); //正常断开 返回-1
                       if(read == -1) {
                           key.cancel();
                       }else {
                           split(buffer1);
                       }
                   }catch (IOException e) {
                       e.printStackTrace();
                       key.cancel(); //因为客户端断开，会有一个read事件 因此需要将key取消，从selector的key集合删除key
                   }
                }
            }
        }
    }
}
