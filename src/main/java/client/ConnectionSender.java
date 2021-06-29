package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ConnectionSender {
    private final Socket clientSocket;
    private final DataOutputStream dataOutputStream;
    private final DataInputStream dataInputStream;

    public ConnectionSender(int port) throws IOException {
        this("localhost", port);
    }

    public ConnectionSender(String host, int port) throws IOException {
        clientSocket = new Socket(host, port);
        dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
        dataInputStream = new DataInputStream(clientSocket.getInputStream());
    }

    public void close() throws IOException {
        dataInputStream.close();
        dataOutputStream.close();
        clientSocket.close();
    }

    public DataOutputStream getDataOutputStream() {
        return dataOutputStream;
    }

    public DataInputStream getDataInputStream() {
        return dataInputStream;
    }
}
