package client;

import exceptions.CommandNotFoundException;
import exceptions.InputException;
import requestbuilders.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.function.Consumer;
import java.util.function.BiConsumer;

public class ClientCore {
    private boolean exitInvoked = false;
    private final HashMap<String, RequestBuilder> availableRequestBuilders = new HashMap<>();
    private final BiConsumer<String, String> addDescription;
    private final Consumer<String[]> addHistoryLine;

    public ClientCore() {
        this(new HashSet<>());
    }

    public ClientCore(HashSet<String> superCoreScripts) {
        Help help = new Help();
        History history = new History();
        addDescription = help::addDescription;
        addHistoryLine = history::addHistoryLine;
        addRequestBuilder(help);
        addRequestBuilder(new Info());
        addRequestBuilder(new Show());
        addRequestBuilder(new Add());
        addRequestBuilder(new Update());
        addRequestBuilder(new RemoveByID());
        addRequestBuilder(new Clear());
        addRequestBuilder(new ExecuteScript(superCoreScripts));
        addRequestBuilder(new Exit(() -> { exitInvoked = true; }));
        addRequestBuilder(new AddIfMax());
        addRequestBuilder(new AddIfMin());
        addRequestBuilder(history);
        addRequestBuilder(new MaxByStatus());
        addRequestBuilder(new PrintAscending());
        addRequestBuilder(new PrintFieldDescendingPosition());
    }

    public void addRequestBuilder(RequestBuilder requestBuilder) {
        String description = requestBuilder.getDescription();
        String requestName = description.split(" ")[0];
        availableRequestBuilders.put(requestName, requestBuilder);
        addDescription.accept(requestName, description);
    }

    public boolean checkExitInvocation() {
        return exitInvoked;
    }

    public Request buildRequest(String input) throws InputException {
        if (input.equals(""))
            return null;
        String[] tokens = input.split(" ");
        for (String requestName : availableRequestBuilders.keySet()) {
            if (requestName.equals(tokens[0])) {
                RequestBuilder requestBuilder = availableRequestBuilders.get(tokens[0]);
                Request request = requestBuilder.build(tokens);
                addHistoryLine.accept(tokens);
                return request;
            }
        }
        throw new CommandNotFoundException(tokens[0]);
    }
}