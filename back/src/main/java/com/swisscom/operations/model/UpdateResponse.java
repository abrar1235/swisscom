package com.swisscom.operations.model;

import lombok.Getter;

@Getter
public class UpdateResponse {

    private final String status;
    private final String message;
    private final int updates;

    public UpdateResponse(int updates) {
        this.status = updates > 0 ? "success" : "failure";
        this.message = updates > 0 ? "Resource Updated, total Updates " + updates : "Something Went Wrong";
        this.updates = updates;
    }


}
