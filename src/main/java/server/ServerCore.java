package server;

import client.Request;
import exceptions.InputException;
import parameters.Worker;
import util.WorkerProvider;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;
import java.util.function.Function;

public class ServerCore {
    private final HashMap<String, Function<Request, String>> availableFunctions = new HashMap<>();
    private final CollectionManager collectionManager;
    private String collectionManagerMessage = "";

    public ServerCore(String filePath) {
        collectionManager = new CollectionManager(filePath, message -> collectionManagerMessage += message + "\n");
        availableFunctions.put("add", request -> {
            long ID = collectionManager.add(request.getWorker());
            return "Элемент успешно добавлен в коллекцию (ID: " + ID + ").";
        });
        availableFunctions.put("add_if_max", request -> {
            Worker worker = request.getWorker();
            Optional<Worker> maxSoFar = collectionManager.elements.stream().max(Worker::compareTo);
            if (!maxSoFar.isPresent() || worker.compareTo(maxSoFar.get()) > 0) {
                long ID = collectionManager.add(worker);
                return "Элемент успешно добавлен в коллекцию (ID: " + ID + ").";
            }
            return "А вот у работника с ID " + maxSoFar.get().getID() + " зарплата не меньше, поэтому я ничего в коллекцию не добавлю.";
        });
        availableFunctions.put("add_if_min", request -> {
            Worker worker = request.getWorker();
            Optional<Worker> minSoFar = collectionManager.elements.stream().min(Worker::compareTo);
            if (!minSoFar.isPresent() || worker.compareTo(minSoFar.get()) < 0) {
                long ID = collectionManager.add(worker);
                return "Элемент успешно добавлен в коллекцию (ID: " + ID + ").";
            }
            return "А вот у работника с ID " + minSoFar.get().getID() + " зарплата не больше, поэтому я ничего в коллекцию не добавлю.";
        });
        availableFunctions.put("clear", request -> {
            collectionManager.clear();
            return "Очистил коллекцию, теперь там пусто.";
        });
        availableFunctions.put("info", request -> collectionManager.getInfo());
        availableFunctions.put("max_by_status", request -> {
            Optional<Worker> maxWorker = collectionManager.elements.stream()
                    .filter(worker -> worker.getStatus() != null).max(Comparator.comparing(Worker::getStatus));
            return maxWorker.map(WorkerProvider::generateInfoString).orElse("Нет никаких элементов в коллекции.");
        });
        availableFunctions.put("print_ascending", request -> {
            TreeSet<Worker> treeSet = new TreeSet<>(collectionManager.elements);
            Optional<String> response = treeSet.stream()
                    .map(WorkerProvider::generateInfoString).map(s -> s + "\n").reduce((a, b) -> a+b);
            return response.orElse("");
        });
        availableFunctions.put("print_field_descending_position", request -> {
            TreeSet<Worker> treeSet = new TreeSet<>(Comparator.comparing(Worker::getPosition).thenComparing(Worker::getSalary));
            treeSet.addAll(collectionManager.elements);
            Optional<String> response = treeSet.stream()
                    .map(WorkerProvider::generateInfoString).map(s -> s + "\n").reduce((a, b) -> a+b);
            return response.orElse("");
        });
        availableFunctions.put("remove_by_id", request -> {
            long ID = Long.parseLong(request.getParameters()[1]);
            if (collectionManager.remove(ID))
                return "Элемент (ID: " + ID + ") успешно удалён.";
            return "Нет элемента с таким ID.";
        });
        availableFunctions.put("show", request -> {
            if (request.getParameters().length < 2) {
                Optional<String> response = collectionManager.elements.stream()
                        .map(WorkerProvider::generateInfoString).map(s -> s + "\n").reduce((a, b) -> a+b);
                return response.orElse("");
            }
            long ID = Long.parseLong(request.getParameters()[1]);
            Worker worker = collectionManager.workersByID.get(ID);
            if (worker == null)
                return "Нет элемента с таким ID.";
            return WorkerProvider.generateInfoString(worker);
        });
        availableFunctions.put("update", request -> {
            long ID = Long.parseLong(request.getParameters()[1]);
            collectionManager.update(ID, request.getWorker());
            return "Элемент (ID: " + ID + ") успешно обновлён.";
        });
        availableFunctions.put("get_collection_manager_message", request -> collectionManagerMessage);
    }

    public Response handleRequest(Request request) {
        String response = availableFunctions.get(request.getRequestName()).apply(request);
        TreeSet<Worker> treeSet = new TreeSet<>((workerA, workerB) -> {
            try {
                ByteArrayOutputStream byteArrayOutputStreamA = new ByteArrayOutputStream();
                ByteArrayOutputStream byteArrayOutputStreamB = new ByteArrayOutputStream();
                ObjectOutputStream objectOutputStreamA = new ObjectOutputStream(byteArrayOutputStreamA);
                ObjectOutputStream objectOutputStreamB = new ObjectOutputStream(byteArrayOutputStreamB);
                objectOutputStreamA.writeObject(workerA);
                objectOutputStreamB.writeObject(workerB);
                objectOutputStreamA.flush();
                objectOutputStreamB.flush();
                int sizeA = byteArrayOutputStreamA.toByteArray().length;
                int sizeB = byteArrayOutputStreamB.toByteArray().length;
                return sizeA-sizeB;
            } catch (IOException exception) {
                return 0;
            }
        });
        treeSet.addAll(collectionManager.elements);
        LinkedHashSet<Worker> sortedElements = new LinkedHashSet<>(treeSet);
        return new Response(response, sortedElements);
    }

    public void forceSave() {
        try {
            collectionManager.save();
        } catch (InputException exception) {
            System.out.println(exception.getMessage());
            return;
        }
        System.out.println("Сохранил коллекцию.");
    }
}
