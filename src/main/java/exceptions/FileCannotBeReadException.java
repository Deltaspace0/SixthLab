package exceptions;

public class FileCannotBeReadException extends InvalidFieldException {
    public FileCannotBeReadException(String filePath) {
        super("то, чтобы этот файл (" + filePath + ") можно было читать");
    }
}
