package util;

import client.InputProvider;
import parameters.Color;
import parameters.Position;
import parameters.Status;

import java.time.LocalDateTime;

public class FieldInputProviders {
    private static InputProvider<Boolean> confirmationInputProviderBuilder(String fieldName) {
        return new InputProvider<>("Надо вводить " + fieldName + "? (Надо написать yes или вообще ничего): ",
                FieldValidators.confirmationValidatorBuilder(fieldName));
    }

    public static InputProvider<Position> positionInputProvider = new EnumFieldBuilder<>("должность", Position.class).inputProvider;
    public static InputProvider<Status>   statusInputProvider   = new EnumFieldBuilder<>("статус",      Status.class).inputProvider;
    public static InputProvider<Color>    colorInputProvider    = new EnumFieldBuilder<>("цвет волос",   Color.class).inputProvider;

    public static InputProvider<Boolean> statusConfirmationInputProvider   = confirmationInputProviderBuilder("статус");
    public static InputProvider<Boolean> personConfirmationInputProvider   = confirmationInputProviderBuilder("персональные данные");
    public static InputProvider<Boolean> locationConfirmationInputProvider = confirmationInputProviderBuilder("место работы");

    public static InputProvider<String> workerNameInputProvider = new InputProvider<>(
            "Введите имя сотрудника: ", FieldValidators.workerNameValidator);

    public static InputProvider<String> locationNameInputProvider = new InputProvider<>(
            "Введите название места работы: ", FieldValidators.locationNameValidator);

    public static InputProvider<Integer> coordinatesXInputProvider = new InputProvider<>(
            "Введите координату X: ", FieldValidators.coordinatesXValidator);

    public static InputProvider<Double> coordinatesYInputProvider = new InputProvider<>(
            "Введите координату Y (максимально 60): ", FieldValidators.coordinatesYValidator);

    public static InputProvider<Integer> salaryInputProvider = new InputProvider<>(
            "Введите зарплату (> 0): ", FieldValidators.salaryValidator);

    public static InputProvider<LocalDateTime> birthdayInputProvider = new InputProvider<>(
            "Введите дату рождения (например, 27.02.2002): ", FieldValidators.birthdayValidator);

    public static InputProvider<Long> locationXInputProvider = new InputProvider<>(
            "Введите координату X у места работы: ", FieldValidators.locationXValidator);

    public static InputProvider<Integer> locationYInputProvider = new InputProvider<>(
            "Введите координату Y у места работы: ", FieldValidators.locationYValidator);

    public static InputProvider<Integer> locationZInputProvider = new InputProvider<>(
            "Введите координату Z у места работы: ", FieldValidators.locationZValidator);
}
