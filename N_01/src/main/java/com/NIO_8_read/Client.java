package com.NIO_7_FUJIAN;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

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
        sc.write(Charset.defaultCharset().encode("0123456789abcdef3333\n"));
        System.in.read();
    }
}
