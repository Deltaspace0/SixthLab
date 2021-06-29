package requestbuilders;

import client.Request;
import exceptions.InputException;

public class Info implements RequestBuilder {

    @Override
    public Request build(String[] tokens) throws InputException {
        return new Request.Builder("info").build();
    }

    @Override
    public String getDescription() {
        return "info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)";
    }
}
