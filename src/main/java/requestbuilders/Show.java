package requestbuilders;

import client.Request;
import exceptions.InputException;
import exceptions.InvalidFieldException;

public class Show implements RequestBuilder {

    @Override
    public Request build(String[] tokens) throws InputException {
        if (tokens.length > 1) {
            try {
                Long.parseLong(tokens[1]);
            } catch (NumberFormatException exception) {
                throw new InvalidFieldException("ID");
            }
        }
        return new Request.Builder("show").addParameters(tokens).build();
    }

    @Override
    public String getDescription() {
        return "show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении";
    }
}
