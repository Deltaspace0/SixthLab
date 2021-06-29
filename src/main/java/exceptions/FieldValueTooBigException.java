package exceptions;

public class FieldValueTooBigException extends InputException {
    public FieldValueTooBigException(String maxValue) {
        super("Не, слишком много! Надо, чтобы было меньше " + maxValue + ".");
    }
}
