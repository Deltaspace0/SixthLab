package requestbuilders;

import client.Request;
import exceptions.InputException;

public class Exit implements RequestBuilder {
    private final Runnable exitInvoker;

    public Exit(Runnable exitInvoker) {
        this.exitInvoker = exitInvoker;
    }

    @Override
    public Request build(String[] tokens) throws InputException {
        exitInvoker.run();
        return null;
    }

    @Override
    public String getDescription() {
        return "exit : завершить программу (без сохранения в файл)";
    }
}
