package exceptions;

public class FieldValueTooSmallException extends InputException {
    public FieldValueTooSmallException(String minValue) {
        super("Не, слишком мало! Надо, чтобы было больше " + minValue + ".");
    }
}
