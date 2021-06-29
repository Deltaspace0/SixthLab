package exceptions;

public class EmptyFieldException extends InputException {
    public EmptyFieldException(String fieldName) {
        super("А где " + fieldName + "?");
    }
}
