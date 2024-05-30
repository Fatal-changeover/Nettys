package com.NIO_3;


import lombok.extern.slf4j.Slf4j;

import javax.lang.model.element.VariableElement;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.ArrayList;
import java.util.Iterator;

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
    public static void main(String[] args) throws IOException {
        //1.创建selector，管理多个channel
        Selector selector = Selector.open();
        ByteBuffer buffer = ByteBuffer.allocate(16);
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        //2.建立selector和channel的联系 ssckey通过这个可以知道事件和哪个channel发生的事件
        /**
         * accept 有链接请求时触发
         * connect 客户端 连接建立后触发
         * read 可读事件
         * write 可写事件
         */
        SelectionKey sscKey = ssc.register(selector, 0, null);
        //指明key只关注accept事件
        sscKey.interestOps(SelectionKey.OP_ACCEPT);
        log.debug("注册事件{}",sscKey);
        ssc.bind(new InetSocketAddress(8080));
        ArrayList<SocketChannel> channels = new ArrayList<>();
        while (true) {
            //3.select方法  无事件就会阻塞 有事件会恢复运行
            selector.select();
            //4.处理事件，selectedKeys集合内部包含了所有发生的事件
            Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                log.debug("发生事件{}",key);
                ServerSocketChannel sscChannel = (ServerSocketChannel) key.channel();
                SocketChannel sc = sscChannel.accept();
                log.debug("建立连接{}",sc);
            }
        }
    }
}
