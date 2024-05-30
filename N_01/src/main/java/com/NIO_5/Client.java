package com.NIO_4_cancel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

/**
 * ClassName: Client
 * Package: com.NIO
 * Description:
 *
 * @Author R
 * @Create 2024/5/30 11:31
 * @Version 1.0
 */
public class Client {
    public static void main(String[] args) throws IOException {
        SocketChannel sc = SocketChannel.open();
        sc.connect(new InetSocketAddress("localhost",8080));
        System.out.println("waiting...");
    }
}
