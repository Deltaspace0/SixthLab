package exceptions;

public class FileDoesNotExistException extends InvalidFieldException {
    public FileDoesNotExistException(String filePath) {
        super("то, чтобы этот файл (" + filePath + ") вообще существовал");
    }
}
