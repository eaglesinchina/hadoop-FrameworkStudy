package nio.nio3.nio2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class Cli {

    public static void main(String[] args) throws IOException, InterruptedException {

        //开启： 客户端通道
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
//        InetSocketAddress addr = new InetSocketAddress("127.0.0.1", 8888);
        InetSocketAddress addr = new InetSocketAddress("192.168.56.1", 8888);
        socketChannel.connect(addr);

        Selector selector = Selector.open();
        SelectionKey key = socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE | SelectionKey.OP_CONNECT);

        ByteBuffer buf = ByteBuffer.allocate(1024);
        int i = 1;

        //循环： 遍历Key
        while (true) {
            selector.select();



                if (key.isConnectable()) {
                    socketChannel.finishConnect();
                    System.out.println("cli2 ..connectable!");
                }

                if (key.isReadable()) {

                    buf.clear();
                    socketChannel.read(buf);
                    buf.flip();

                    System.out.println("cli2--接收消息:  " + new String(buf.array(), 0, buf.limit()));
                }

                if (key.isWritable()) {
                    socketChannel.write(ByteBuffer.wrap(("cli2--ser: " + (i++)).getBytes()));
                    System.out.println("cli2: 发送消息完毕！");

                    Thread.sleep(1000);
                }

                //clear key
                selector.selectedKeys().clear();
            }
    }
}
