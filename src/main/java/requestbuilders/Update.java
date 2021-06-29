package requestbuilders;

import client.Request;
import exceptions.EmptyFieldException;
import exceptions.InputException;
import exceptions.InvalidFieldException;
import parameters.Worker;
import util.WorkerProvider;

public class Update implements RequestBuilder {

    @Override
    public Request build(String[] tokens) throws InputException {
        if (tokens.length > 1) {
            try {
                Long.parseLong(tokens[1]);
            } catch (NumberFormatException exception) {
                throw new InvalidFieldException("ID");
            }
        } else {
            throw new EmptyFieldException("ID");
        }
        Worker worker = WorkerProvider.getWorker();
        return new Request.Builder("update").addParameters(tokens).addWorker(worker).build();
    }

    @Override
    public String getDescription() {
        return "update id {element} : обновить значение элемента коллекции, id которого равен заданному";
    }
}
