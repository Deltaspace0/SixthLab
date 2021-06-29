package server;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ObjectReceiver<T> {
    private final int defaultBufferSize;

    public ObjectReceiver() {
        this(65536);
    }

    public ObjectReceiver(int defaultBufferSize) {
        this.defaultBufferSize = defaultBufferSize;
    }

    public T receive(SocketChannel client) throws IOException, ClassNotFoundException {
        return receive(client, defaultBufferSize);
    }

    public T receive(SocketChannel client, int bufferSize) throws IOException, ClassNotFoundException {
        ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
        client.read(buffer);
        buffer.flip();
        byte[] data = new byte[buffer.remaining()];
        buffer.get(data);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        T object = (T) objectInputStream.readObject();
        byteArrayInputStream.close();
        objectInputStream.close();
        return object;
    }
}
