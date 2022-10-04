package com.swisscom.operations.exceptions;

public class IllegalDateException extends Exception {

    public IllegalDateException() {
        super();
    }

    public IllegalDateException(String message) {
        super(message);
    }

    public IllegalDateException(String message, Throwable cause) {
        super(message, cause);
    }
}
