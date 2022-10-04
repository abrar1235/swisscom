package com.swisscom.operations.model;

import lombok.Getter;
import org.springframework.dao.DataIntegrityViolationException;

@Getter
public class Failure {

    private String error;

    public Failure(String error) {
        this.error = error;
    }

    public Failure(Exception e) {
        if (e.getClass().equals(DataIntegrityViolationException.class)) this.error = "Email Already Exist";
    }
}
