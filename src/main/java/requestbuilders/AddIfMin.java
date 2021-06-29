package requestbuilders;

import client.Request;
import exceptions.InputException;
import parameters.Worker;
import util.WorkerProvider;

public class AddIfMin implements RequestBuilder {

    @Override
    public Request build(String[] tokens) throws InputException {
        Worker worker = WorkerProvider.getWorker();
        return new Request.Builder("add_if_min").addWorker(worker).build();
    }

    @Override
    public String getDescription() {
        return "add_if_min {element} : добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции";
    }
}
