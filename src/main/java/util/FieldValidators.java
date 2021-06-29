package util;

import client.Validator;
import exceptions.*;
import parameters.Color;
import parameters.Position;
import parameters.Status;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FieldValidators {
    private static final Pattern birthdayPattern = Pattern.compile("^\\d{2}\\.\\d{2}\\.\\d{4}$");

    public static Validator<Boolean> confirmationValidatorBuilder(String fieldName) {
        return input -> {
            if (input.equals(""))
                return false;
            if (input.equalsIgnoreCase("yes"))
                return true;
            throw new InvalidFieldException("yes, так надо вводить " + fieldName + " или всё-таки нет?");
        };
    }

    public static Validator<Integer> integerCoordinateValidator = input -> {
        if (input.equals(""))
            throw new EmptyFieldException("координата");
        try {
            return Integer.valueOf(input);
        } catch (NumberFormatException exception) {
            throw new InvalidFieldException("координату");
        }
    };

    public static Validator<Position> positionValidator = new EnumFieldBuilder<>("должность", Position.class).validator;
    public static Validator<Status>   statusValidator   = new EnumFieldBuilder<>("статус",      Status.class).validator;
    public static Validator<Color>    colorValidator    = new EnumFieldBuilder<>("цвет волос",   Color.class).validator;

    public static Validator<Boolean> statusConfirmationValidator   = confirmationValidatorBuilder("статус");
    public static Validator<Boolean> personConfirmationValidator   = confirmationValidatorBuilder("персональные данные");
    public static Validator<Boolean> locationConfirmationValidator = confirmationValidatorBuilder("место работы");

    public static Validator<String> workerNameValidator = input -> {
        if (input.equals(""))
            throw new EmptyFieldException("имя");
        return input;
    };

    public static Validator<String> locationNameValidator = input -> {
        if (input.equals(""))
            throw new EmptyFieldException("название");
        return input;
    };

    public static Validator<Integer> coordinatesXValidator = integerCoordinateValidator;

    public static Validator<Double> coordinatesYValidator = input -> {
        if (input.equals(""))
            throw new EmptyFieldException("координата");
        double y;
        try {
            y = Double.parseDouble(input);
        } catch (NumberFormatException exception) {
            throw new InvalidFieldException("координату");
        }
        if (y > 60)
            throw new FieldValueTooBigException("60");
        return y;
    };

    public static Validator<Integer> salaryValidator = input -> {
        if (input.equals(""))
            throw new EmptyFieldException("зарплата");
        int salary;
        try {
            salary = Integer.parseInt(input);
        } catch (NumberFormatException exception) {
            throw new InvalidFieldException("зарплату");
        }
        if (salary <= 0)
            throw new FieldValueTooSmallException("0");
        return salary;
    };

    public static Validator<LocalDateTime> birthdayValidator = input -> {
        if (input.equals(""))
            throw new EmptyFieldException("дата рождения");
        Matcher matcher = birthdayPattern.matcher(input);
        if (!matcher.matches())
            throw new InvalidFieldException("дату рождения");
        String[] numbers = input.split("\\.");
        int day   = Integer.parseInt(numbers[0]);
        int month = Integer.parseInt(numbers[1]);
        int year  = Integer.parseInt(numbers[2]);
        try {
            return LocalDateTime.of(year, month, day, 0, 0);
        } catch (DateTimeException exception) {
            throw new InvalidFieldException("дату рождения");
        }
    };

    public static Validator<Long> locationXValidator = input -> {
        if (input.equals(""))
            throw new EmptyFieldException("координата");
        try {
            return Long.valueOf(input);
        } catch (NumberFormatException exception) {
            throw new InvalidFieldException("координату");
        }
    };

    public static Validator<Integer> locationYValidator = integerCoordinateValidator;
    public static Validator<Integer> locationZValidator = integerCoordinateValidator;
}
