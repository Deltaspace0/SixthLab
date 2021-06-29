package requestbuilders;

import client.Request;
import exceptions.InputException;

public class PrintAscending implements RequestBuilder {

    @Override
    public Request build(String[] tokens) throws InputException {
        return new Request.Builder("print_ascending").build();
    }

    @Override
    public String getDescription() {
        return "print_ascending : вывести элементы коллекции в порядке возрастания";
    }
}
