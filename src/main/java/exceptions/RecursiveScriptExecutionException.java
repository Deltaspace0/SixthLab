package exceptions;

public class RecursiveScriptExecutionException extends InputException {
    public RecursiveScriptExecutionException(String fileName) {
        super("При попытке исполнить скрипт в файле " + fileName + " был зафиксирован рекурсивный вызов, который привёл бы к бесконечному циклу. Запуск скрипта остановлен.");
    }
}
