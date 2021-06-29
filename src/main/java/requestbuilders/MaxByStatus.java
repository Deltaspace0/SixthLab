package requestbuilders;

import client.Request;
import exceptions.InputException;

public class MaxByStatus implements RequestBuilder {

    @Override
    public Request build(String[] tokens) throws InputException {
        return new Request.Builder("max_by_status").build();
    }

    @Override
    public String getDescription() {
        return "max_by_status : вывести любой объект из коллекции, значение поля status которого является максимальным";
    }
}
