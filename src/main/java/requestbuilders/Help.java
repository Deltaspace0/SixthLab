package requestbuilders;

import client.Request;
import exceptions.InputException;

import java.util.HashMap;

public class Help implements RequestBuilder {
    private StringBuilder defaultHelpString = new StringBuilder();
    private final HashMap<String, String> descriptions = new HashMap<>();

    public void addDescription(String requestName, String description) {
        descriptions.put(requestName, description);
        defaultHelpString.append(description).append("\n");
    }

    @Override
    public Request build(String[] tokens) throws InputException {
        if (tokens.length > 1) {
            if (descriptions.containsKey(tokens[1])) {
                System.out.println(descriptions.get(tokens[1]));
            } else {
                System.out.println("По команде " + tokens[1] + "справки нет.");
            }
        } else {
            System.out.println(defaultHelpString);
        }
        return null;
    }

    @Override
    public String getDescription() {
        return "help : вывести справку по доступным командам";
    }
}
