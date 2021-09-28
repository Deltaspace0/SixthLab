package server;

import client.Request;

import java.io.EOFException;
import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class RequestHandler {
    private final ServerCore serverCore;
    private final ObjectSender<Response> responseSender = new ObjectSender<>();
    private final ObjectReceiver<Request> requestReceiver = new ObjectReceiver<>();

    public RequestHandler(ServerCore serverCore) {
        this.serverCore = serverCore;
    }

    public boolean handle(SelectionKey key) {
        SocketChannel client = (SocketChannel) key.channel();
        Request request;
        try {
            request = requestReceiver.receive(client);
        } catch (EOFException exception) {
            return true;
        } catch (IOException|ClassNotFoundException exception) {
            System.out.println("Не смог получить запрос:");
            exception.printStackTrace();
            return false;
        }
        Response response = serverCore.handleRequest(request);
        try {
            responseSender.send(client, response);
        } catch (IOException exception) {
            System.out.println("Не смог отправить ответ:");
            exception.printStackTrace();
        }
        return false;
    }

    public void forceSave() {
        serverCore.forceSave();
    }
}
