package requestbuilders;

import client.*;
import exceptions.EmptyFieldException;
import exceptions.InputException;
import exceptions.RecursiveScriptExecutionException;
import util.FileProcessor;

import java.util.HashSet;

public class ExecuteScript implements RequestBuilder {
    private final HashSet<String> superCoreScripts;

    public ExecuteScript(HashSet<String> superCoreScripts) {
        this.superCoreScripts = superCoreScripts;
    }

    @Override
    public Request build(String[] tokens) throws InputException {
        if (tokens.length < 2)
            throw new EmptyFieldException("путь к файлу");
        String script = FileProcessor.readFromFile(tokens[1]);
        if (superCoreScripts.contains(script))
            throw new RecursiveScriptExecutionException(tokens[1]);
        HashSet<String> subCoreScripts = new HashSet<>(superCoreScripts);
        subCoreScripts.add(script);
        ClientCore subCore = new ClientCore(subCoreScripts);
        System.out.println("Исполняю скрипт " + tokens[1] + "...\n");
        String[] lines = script.split("\\r?\\n");
        int previousSize = InterScanner.countInterLines();
        for (int i = lines.length-1; i >= 0; i--)
            InterScanner.interfere(lines[i]);
        while (InterScanner.countInterLines() > previousSize) {
            InputProvider<Request> inputProvider = new InputProvider<>("Введите команду: ", subCore::buildRequest, () -> InterScanner.countInterLines() == previousSize);
            Request request = inputProvider.provide();
            if (subCore.checkExitInvocation()) {
                while (InterScanner.countInterLines() != previousSize)
                    InterScanner.nextLine(false);
            }
            if (request == null)
                continue;
            Main.sendRequest(request);
        }
        System.out.println("\nСкрипт " + tokens[1] + " закончил своё выполнение, выхожу из него...");
        return null;
    }

    @Override
    public String getDescription() {
        return "execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.";
    }
}
