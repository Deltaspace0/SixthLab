package client;

import java.io.*;

public class ObjectSender<T> {
    private final DataOutputStream dataOutputStream;
    private byte[] data = null;

    public ObjectSender(DataOutputStream dataOutputStream) {
        this.dataOutputStream = dataOutputStream;
    }

    public int write(T object) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(object);
        objectOutputStream.flush();
        data = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();
        objectOutputStream.close();
        return data.length;
    }

    public void send() throws IOException {
        dataOutputStream.write(data);
    }

    public void send(T object) throws IOException {
        write(object);
        send();
    }
}