package server;

import parameters.Worker;

import java.io.Serializable;
import java.util.LinkedHashSet;

public class Response implements Serializable {
    private final String response;
    private final LinkedHashSet<Worker> elements;

    public Response(String response, LinkedHashSet<Worker> elements) {
        this.response = response;
        this.elements = elements;
    }

    public String getResponse() {
        return response;
    }

    public LinkedHashSet<Worker> getElements() {
        return elements;
    }
}
