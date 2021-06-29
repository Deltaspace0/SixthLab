package exceptions;

public class InvalidFieldException extends InputException {
    public InvalidFieldException(String fieldName) {
        super("Не, это не похоже на " + fieldName + ".");
    }
}
