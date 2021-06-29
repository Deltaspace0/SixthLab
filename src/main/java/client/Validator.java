package client;

import exceptions.InputException;

public interface Validator<T> {
    T get(String input) throws InputException;
}
