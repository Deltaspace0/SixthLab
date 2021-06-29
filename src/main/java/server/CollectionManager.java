package server;

import exceptions.FileDoesNotExistException;
import exceptions.InputException;
import parameters.Worker;
import util.FileProcessor;
import util.WorkerProvider;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.function.Consumer;

public class CollectionManager {
    public final LinkedHashSet<Worker> elements = new LinkedHashSet<>();
    public final HashMap<Long, Worker> workersByID = new HashMap<>();
    private final String filePath;
    private LocalDateTime initializationDate;
    private long nextID;

    public CollectionManager(String filePath, Consumer<String> sendMessage) {
        this.filePath = filePath;
        boolean createNew = false;
        try {
            String[] lines = FileProcessor.readFromFile(filePath).split("\\r?\\n");
            String[] meta = lines[0].split(",");
            initializationDate = LocalDateTime.parse(meta[0], DateTimeFormatter.ofPattern("dd.MM.yyyy.HH.mm"));
            nextID = Long.parseLong(meta[1]);
            for (int i = 1; i < lines.length; i++) {
                Worker worker;
                try {
                    worker = WorkerProvider.getWorkerFromCSV(lines[i]);
                } catch (InputException exception) {
                    sendMessage.accept("При загрузке работника возникла ошибка: " + exception.getMessage());
                    continue;
                }
                elements.add(worker);
                workersByID.put(worker.getID(), worker);
            }
        } catch (FileDoesNotExistException exception) {
            sendMessage.accept("Внимание! Файла не было, поэтому вот новая коллекция.");
            createNew = true;
        } catch (InputException exception) {
            sendMessage.accept(exception.getMessage());
            createNew = true;
        } catch (RuntimeException exception) {
            exception.printStackTrace();
            sendMessage.accept("\nКороче файл попорчен, поэтому вот новая коллекция.");
            createNew = true;
        }
        if (createNew) {
            initializationDate = LocalDateTime.now();
            clear();
        }
    }

    public String getInfo() {
        return "Тип коллекции: LinkedHashSet" +
                "\nДата инициализации: " + DateTimeFormatter.ofPattern("hh:mm dd.MM.yyyy").format(initializationDate) +
                "\nКоличество элементов: " + elements.size() +
                "\nЗагружена из файла: " + filePath +
                "\nСледующий элемент будет иметь ID " + nextID;
    }

    public long add(Worker worker) {
        while (workersByID.containsKey(nextID))
            nextID++;
        worker.setID(nextID);
        worker.setCreationDate(ZonedDateTime.now());
        elements.add(worker);
        workersByID.put(nextID, worker);
        return nextID++;
    }

    public void update(long ID, Worker worker) {
        Worker oldWorker = workersByID.get(ID);
        ZonedDateTime creationDate = ZonedDateTime.now();
        if (oldWorker != null) {
            creationDate = oldWorker.getCreationDate();
            elements.remove(oldWorker);
        }
        worker.setID(ID);
        worker.setCreationDate(creationDate);
        elements.add(worker);
        workersByID.put(ID, worker);
    }

    public boolean remove(long ID) {
        Worker worker = workersByID.get(ID);
        if (worker == null)
            return false;
        elements.remove(worker);
        workersByID.remove(ID);
        if (ID == nextID-1)
            nextID--;
        return true;
    }

    public void clear() {
        elements.clear();
        workersByID.clear();
        nextID = 1;
    }

    public void save() throws InputException {
        StringBuilder csv = new StringBuilder();
        csv.append(DateTimeFormatter.ofPattern("dd.MM.yyyy.HH.mm").format(initializationDate));
        csv.append(",").append(nextID).append(",,,,,,,,,,,,\n");
        for (Worker worker : elements)
            csv.append(WorkerProvider.convertToString(worker)).append("\n");
        FileProcessor.saveToFile(filePath, csv.toString().trim());
    }
}
