package requestbuilders;

import client.Request;
import exceptions.InputException;

public class PrintFieldDescendingPosition implements RequestBuilder {

    @Override
    public Request build(String[] tokens) throws InputException {
        return new Request.Builder("print_field_descending_position").build();
    }

    @Override
    public String getDescription() {
        return "print_field_descending_position : вывести значения поля position всех элементов в порядке убывания";
    }
}
