package exceptions;

public class FileCannotBeReadAndWrittenException extends InvalidFieldException {
    public FileCannotBeReadAndWrittenException(String filePath) {
        super("то, чтобы можно было этот файл (" + filePath + ") читать и писать туда");
    }
}
