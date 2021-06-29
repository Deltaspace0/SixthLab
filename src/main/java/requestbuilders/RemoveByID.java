package requestbuilders;

import client.Request;
import exceptions.EmptyFieldException;
import exceptions.InputException;
import exceptions.InvalidFieldException;

public class RemoveByID implements RequestBuilder {

    @Override
    public Request build(String[] tokens) throws InputException {
        if (tokens.length > 1) {
            try {
                Long.parseLong(tokens[1]);
                return new Request.Builder("remove_by_id").addParameters(tokens).build();
            } catch (NumberFormatException exception) {
                throw new InvalidFieldException("ID");
            }
        } else {
            throw new EmptyFieldException("ID");
        }
    }

    @Override
    public String getDescription() {
        return "remove_by_id id : удалить элемент из коллекции по его id";
    }
}
