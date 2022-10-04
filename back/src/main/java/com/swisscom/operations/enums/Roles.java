package com.swisscom.operations.enums;

import java.util.List;

public enum Roles {

    USER, ADMIN;

    public static List<String> asList() {
        return List.of(USER.toString(), ADMIN.toString());
    }
}
