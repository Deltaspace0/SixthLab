package exceptions;

public class NotFileButDirectoryException extends InvalidFieldException {
    public NotFileButDirectoryException(String filePath) {
        super("файл, это вообще-то директория (" + filePath + ")");
    }
}
