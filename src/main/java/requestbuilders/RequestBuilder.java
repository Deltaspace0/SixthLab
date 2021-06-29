package requestbuilders;

import client.Request;
import exceptions.InputException;

public interface RequestBuilder {
    Request build(String[] tokens) throws InputException;
    String getDescription();
}
