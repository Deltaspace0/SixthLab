package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Function;

public class ConnectionReceiver {
    private final Selector selector;
    private final ServerSocketChannel serverSocketChannel;
    private final Function<SelectionKey, Boolean> ifReadableDefault;
    private final long timeoutDefault;
    private final HashSet<Integer> keyHashCodes = new HashSet<>();
    private boolean working = true;

    public ConnectionReceiver(int port, Function<SelectionKey, Boolean> ifReadableDefault, long timeoutDefault) throws IOException {
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress("localhost", port));
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        this.ifReadableDefault = ifReadableDefault;
        this.timeoutDefault = timeoutDefault;
    }

    public void run() throws IOException {
        run(ifReadableDefault, timeoutDefault);
    }

    public void run(Function<SelectionKey, Boolean> ifReadable, long timeout) throws IOException {
        selector.select(timeout);
        Set<SelectionKey> selectedKeys = selector.selectedKeys();
        Iterator<SelectionKey> iterator = selectedKeys.iterator();
        while (iterator.hasNext()) {
            SelectionKey key = iterator.next();
            if (key.isAcceptable()) {
                SocketChannel client = serverSocketChannel.accept();
                client.configureBlocking(false);
                SelectionKey clientKey = client.register(selector, SelectionKey.OP_READ);
                System.out.println("Клиент подсоединился: " + clientKey.hashCode());
                keyHashCodes.add(clientKey.hashCode());
            }
            if (key.isReadable()) {
                boolean closed = ifReadable.apply(key);
                if (closed) {
                    System.out.println("Клиент отсоединился: " + key.hashCode());
                    keyHashCodes.remove(key.hashCode());
                    key.channel().close();
                }
            }
            iterator.remove();
        }
    }

    public void showKeyHashCodes() {
        System.out.println("Подключенные клиенты:");
        keyHashCodes.forEach(x -> System.out.println("- " + x));
    }

    public boolean isWorking() {
        return working;
    }

    public void shutdown() {
        working = false;
        try {
            serverSocketChannel.close();
        } catch (IOException exception) {
            System.out.println(".");
        }
    }
}
