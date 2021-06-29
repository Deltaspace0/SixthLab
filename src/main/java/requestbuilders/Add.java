package requestbuilders;

import client.Request;
import exceptions.InputException;
import parameters.Worker;
import util.WorkerProvider;

public class Add implements RequestBuilder {

    @Override
    public Request build(String[] tokens) throws InputException {
        Worker worker = WorkerProvider.getWorker();
        return new Request.Builder("add").addWorker(worker).build();
    }

    @Override
    public String getDescription() {
        return "add {element} : добавить новый элемент в коллекцию";
    }
}
