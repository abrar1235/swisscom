package com.swisscom.operations.model;

import lombok.Getter;

@Getter
public class DeleteResponse {

    private final String status;
    private final String message;
    private final int deletes;

    public DeleteResponse(int deletes){
        this.status = deletes > 0 ? "Success" : "Failure";
        this.message = deletes > 0 ? "Resource Deleted, total Deletes "+deletes :"Something Went Wrong";
        this.deletes = deletes;
    }
}
