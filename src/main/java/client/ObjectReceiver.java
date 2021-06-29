package client;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class ObjectReceiver<T> {
    private final DataInputStream dataInputStream;
    private final int defaultBufferSize;

    public ObjectReceiver(DataInputStream dataInputStream) {
        this(dataInputStream, 65536);
    }

    public ObjectReceiver(DataInputStream dataInputStream, int defaultBufferSize) {
        this.dataInputStream = dataInputStream;
        this.defaultBufferSize = defaultBufferSize;
    }

    public T receive() throws IOException, ClassNotFoundException {
        return receive(defaultBufferSize);
    }

    public T receive(int bufferSize) throws IOException, ClassNotFoundException {
        byte[] buffer = new byte[bufferSize];
        dataInputStream.read(buffer);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buffer);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        T object = (T) objectInputStream.readObject();
        byteArrayInputStream.close();
        objectInputStream.close();
        return object;
    }
}
