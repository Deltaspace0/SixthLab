package exceptions;

public class CommandNotFoundException extends InputException {
    public CommandNotFoundException(String commandName) {
        super("К сожалению, нет такой команды: " + commandName);
    }
}
