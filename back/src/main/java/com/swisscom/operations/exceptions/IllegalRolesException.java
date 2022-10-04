package com.swisscom.operations.exceptions;

public class IllegalRolesException extends Exception {

    public IllegalRolesException() {
        super();
    }

    public IllegalRolesException(String message) {
        super(message);
    }

    public IllegalRolesException(String message, Throwable cause) {
        super(message, cause);
    }
}
