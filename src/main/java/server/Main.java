package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.nio.channels.CancelledKeyException;

public class Main {
    public static void main(String[] args) {
        RequestHandler requestHandler = new RequestHandler(new ServerCore(System.getenv("THETABLE")));
        ConnectionReceiver connectionReceiver;
        try {
            connectionReceiver = new ConnectionReceiver(4242, requestHandler::handle, 100);
        } catch (IOException exception) {
            System.out.println("Не смог инициализировать модуль приёма подключений:");
            exception.printStackTrace();
            return;
        }
        System.out.println("Выбран порт 4242");
        String helpString = "Доступные команды: save, show, exit и help.";
        System.out.println(helpString);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (connectionReceiver.isWorking()) {
            try {
                if (br.ready()) {
                   String line = br.readLine();
                    if (line.equals("save"))
                        requestHandler.forceSave();
                    if (line.equals("show"))
                        connectionReceiver.showKeyHashCodes();
                    if (line.equals("exit")) {
                        requestHandler.forceSave();
                        connectionReceiver.shutdown();
                    }
                    if (line.equals("help")) {
                        System.out.println(helpString);
                    }
                }
                connectionReceiver.run();
            } catch (IOException exception) {
                System.out.println("Произошла ошибка при исполнении основного цикла:");
                exception.printStackTrace();
                break;
            } catch (CancelledKeyException exception) {
                System.out.println("-");
            }
        }
    }
}
