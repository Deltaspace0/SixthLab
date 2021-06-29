package requestbuilders;

import client.Request;
import exceptions.InputException;
import parameters.Worker;
import util.WorkerProvider;

public class AddIfMax implements RequestBuilder {

    @Override
    public Request build(String[] tokens) throws InputException {
        Worker worker = WorkerProvider.getWorker();
        return new Request.Builder("add_if_max").addWorker(worker).build();
    }

    @Override
    public String getDescription() {
        return "add_if_max {element} : добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции";
    }
}
