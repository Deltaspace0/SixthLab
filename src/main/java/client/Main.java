package client;

import exceptions.ShutdownException;
import server.Response;

import java.io.IOException;
import java.io.StreamCorruptedException;

public class Main {
    private static ConnectionSender connectionSender;
    private static ObjectSender<Request> requestSender;
    private static ObjectReceiver<Response> responseReceiver;

    public static void main(String[] args) {
        if (!connect())
            return;
        ClientCore clientCore = new ClientCore();
        sendRequest(new Request.Builder("get_collection_manager_message").build());
        InputProvider<Request> inputProvider = new InputProvider<>("Введите команду: ", clientCore::buildRequest);
        while (true) {
            try {
                Request request = inputProvider.provide();
                if (clientCore.checkExitInvocation())
                    break;
                if (request == null)
                    continue;
                sendRequest(request);
            } catch (ShutdownException exception) {
                break;
            }
        }
    }

    public static boolean reconnect() {
        try {
            connectionSender.close();
        } catch (IOException exception) {
            System.out.println("Не смог закрыть connectionSender:");
            exception.printStackTrace();
            return false;
        }
        return connect();
    }

    private static boolean connect() {
        connectionSender = null;
        while (connectionSender == null) {
            try {
                connectionSender = new ConnectionSender(4242);
            } catch (IOException exception) {
                System.out.println("Не смог установить соединение с сервером:");
                exception.printStackTrace();
                if (needToStop())
                    return false;
            }
        }
        requestSender = new ObjectSender<>(connectionSender.getDataOutputStream());
        responseReceiver = new ObjectReceiver<>(connectionSender.getDataInputStream());
        return true;
    }

    public static void sendRequest(Request request) {
        boolean success = false;
        while (!success) {
            success = true;
            try {
                requestSender.send(request);
            } catch (IOException exception) {
                success = false;
                System.out.println("Не смог отправить запрос на сервер:");
                exception.printStackTrace();
                if (needToStop())
                    break;
            }
            if (success) {
                try {
                    Response response = responseReceiver.receive();
                    System.out.println(response.getResponse());
                    if (request.getParameters() != null && request.getParameters().length == 7) {
                        response.getElements().forEach(worker -> System.out.print(worker.getID()));
                        System.out.println();
                    }
                } catch (StreamCorruptedException exception) {
                    success = false;
                    exception.printStackTrace();
                    System.out.println("Что-то сервер отрубился, переподключиться? [Y/n] ");
                    if (InterScanner.nextLineForced().equals("n") || !reconnect())
                        throw new ShutdownException();
                } catch (IOException|ClassNotFoundException exception) {
                    success = false;
                    System.out.println("Не смог получить ответ:");
                    exception.printStackTrace();
                    if (needToStop())
                        break;
                }
            }
        }
    }

    private static boolean needToStop() {
        System.out.println("Попробовать ещё раз? [Y/n] ");
        return InterScanner.nextLineForced().equals("n");
    }
}
