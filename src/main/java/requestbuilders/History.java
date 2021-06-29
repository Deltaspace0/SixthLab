package requestbuilders;

import client.Request;
import exceptions.InputException;

import java.util.LinkedList;

public class History implements RequestBuilder {
    private final LinkedList<String> requestHistory = new LinkedList<>();

    public void addHistoryLine(String[] tokens) {
        requestHistory.add(tokens[0]);
        if (requestHistory.size() > 9)
            requestHistory.remove();
    }

    @Override
    public Request build(String[] tokens) throws InputException {
        for (String requestName : requestHistory) {
            System.out.println(requestName);
        }
        return null;
    }

    @Override
    public String getDescription() {
        return "history : вывести последние 9 команд (без их аргументов)";
    }
}
