package util;

import exceptions.InputException;
import parameters.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class WorkerProvider {
    public static Worker getWorker() {
        Worker worker = new Worker();
        worker.setName(FieldInputProviders.workerNameInputProvider.provide());
        Coordinates coordinates = new Coordinates();
        coordinates.setX(FieldInputProviders.coordinatesXInputProvider.provide());
        coordinates.setY(FieldInputProviders.coordinatesYInputProvider.provide());
        worker.setCoordinates(coordinates);
        worker.setSalary(FieldInputProviders.salaryInputProvider.provide());
        worker.setPosition(FieldInputProviders.positionInputProvider.provide());
        if (FieldInputProviders.statusConfirmationInputProvider.provide())
            worker.setStatus(FieldInputProviders.statusInputProvider.provide());
        if (FieldInputProviders.personConfirmationInputProvider.provide()) {
            Person person = new Person();
            person.setBirthday(FieldInputProviders.birthdayInputProvider.provide());
            person.setHairColor(FieldInputProviders.colorInputProvider.provide());
            if (FieldInputProviders.locationConfirmationInputProvider.provide()) {
                Location location = new Location();
                location.setName(FieldInputProviders.locationNameInputProvider.provide());
                location.setX(FieldInputProviders.locationXInputProvider.provide());
                location.setY(FieldInputProviders.locationYInputProvider.provide());
                location.setZ(FieldInputProviders.locationZInputProvider.provide());
                person.setLocation(location);
            }
            worker.setPerson(person);
        }
        return worker;
    }

    public static Worker getWorkerFromCSV(String line) throws InputException {
        Worker worker = new Worker();
        ArrayList<String> tokens = CSVStringBuilder.parse(line);
        worker.setID(Long.parseLong(tokens.get(0)));
        worker.setName(tokens.get(1));
        Coordinates coordinates = new Coordinates();
        coordinates.setX(FieldValidators.coordinatesXValidator.get(tokens.get(2)));
        coordinates.setY(FieldValidators.coordinatesYValidator.get(tokens.get(3)));
        worker.setCoordinates(coordinates);
        worker.setCreationDate(ZonedDateTime.of(LocalDateTime.parse(tokens.get(4),
                DateTimeFormatter.ofPattern("dd.MM.yyyy.HH.mm")), ZoneId.systemDefault()));
        worker.setSalary(FieldValidators.salaryValidator.get(tokens.get(5)));
        worker.setPosition(FieldValidators.positionValidator.get(tokens.get(6)));
        if (!tokens.get(7).equals(""))
            worker.setStatus(FieldValidators.statusValidator.get(tokens.get(7)));
        if (!tokens.get(8).equals("")) {
            Person person = new Person();
            person.setBirthday(FieldValidators.birthdayValidator.get(tokens.get(8)));
            person.setHairColor(FieldValidators.colorValidator.get(tokens.get(9)));
            if (!tokens.get(10).equals("")) {
                Location location = new Location();
                location.setName(tokens.get(10));
                location.setX(FieldValidators.locationXValidator.get(tokens.get(11)));
                location.setY(FieldValidators.locationYValidator.get(tokens.get(12)));
                location.setZ(FieldValidators.locationZValidator.get(tokens.get(13)));
                person.setLocation(location);
            }
            worker.setPerson(person);
        }
        return worker;
    }

    public static String convertToString(Worker worker) {
        CSVStringBuilder csvStringBuilder = new CSVStringBuilder();
        csvStringBuilder = csvStringBuilder
                .append(String.valueOf(worker.getID()))
                .append(worker.getName())
                .append(String.valueOf(worker.getCoordinates().getX()))
                .append(String.valueOf(worker.getCoordinates().getY()))
                .append(DateTimeFormatter.ofPattern("dd.MM.yyyy.HH.mm").format(worker.getCreationDate()))
                .append(String.valueOf(worker.getSalary()))
                .append(worker.getPosition().name())
                .append(worker.getStatus() == null ? "" : worker.getStatus().name());
        if (worker.getPerson() == null) {
            csvStringBuilder = csvStringBuilder.append("").append("").append("").append("").append("").append("");
        } else {
            csvStringBuilder = csvStringBuilder
                    .append(DateTimeFormatter.ofPattern("dd.MM.yyyy").format(worker.getPerson().getBirthday()))
                    .append(worker.getPerson().getHairColor().name());
            if (worker.getPerson().getLocation() == null) {
                csvStringBuilder = csvStringBuilder.append("").append("").append("").append("");
            } else {
                csvStringBuilder = csvStringBuilder
                        .append(worker.getPerson().getLocation().getName())
                        .append(String.valueOf(worker.getPerson().getLocation().getX()))
                        .append(String.valueOf(worker.getPerson().getLocation().getY()))
                        .append(String.valueOf(worker.getPerson().getLocation().getZ()));
            }
        }
        return csvStringBuilder.build();
    }

    public static String generateInfoString(Worker worker) {
        return "\nID: " + worker.getID() +
                "\nИмя сотрудника: " + worker.getName() +
                "\nКоординаты:" +
                "\n\tX: " + worker.getCoordinates().getX() +
                "\n\tY: " + worker.getCoordinates().getY() +
                "\nВремя создания: " + DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy").format(worker.getCreationDate()) +
                "\nЗарплата: " + worker.getSalary() +
                "\nДолжность: " + worker.getPosition() +
                "\nСтатус: " + (worker.getStatus() == null ? "null" : worker.getStatus()) +
                "\nПерсональные данные: " + (worker.getPerson() == null ? "null" :
                "\n\tДата рождения: " + DateTimeFormatter.ofPattern("dd.MM.yyyy").format(worker.getPerson().getBirthday()) +
                        "\n\tЦвет волос: " + worker.getPerson().getHairColor() +
                        "\n\tМесто работы: " + (worker.getPerson().getLocation() == null ? "null" :
                        "\n\t\tНазвание: " + worker.getPerson().getLocation().getName() +
                                "\n\t\tX: " + worker.getPerson().getLocation().getX() +
                                "\n\t\tY: " + worker.getPerson().getLocation().getY() +
                                "\n\t\tZ: " + worker.getPerson().getLocation().getZ())) + "\n";
    }
}
