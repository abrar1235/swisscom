package com.swisscom.operations.enums;

import java.util.List;

public enum Status {

    ACTIVE, INACTIVE, LOCKED;

    public static List<String> asList() {
        return List.of(ACTIVE.toString(), INACTIVE.toString(), LOCKED.toString());
    }
}
