package com.swisscom.operations.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Maintenance {
    private static final String MESSAGE = "Cannot be Blank or Null";
    private String id;

    private String description;

    @NotNull(message = MESSAGE)
    @NotEmpty(message = MESSAGE)
    @NotEmpty()
    private String startTime;

    @NotNull(message = MESSAGE)
    @NotEmpty(message = MESSAGE)
    private String endTime;

    @NotNull(message = MESSAGE)
    @NotEmpty(message = MESSAGE)
    private String timeZone;

    @NotNull(message = MESSAGE)
    @NotEmpty(message = MESSAGE)
    private String userId;
}