package com.swisscom.operations.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;

import static com.swisscom.operations.constant.Messages.*;

@Data
@Builder
public class Credentials {

    @NotNull(message = EMPTY_OR_NULL_MESSAGE)
    @NotEmpty(message = EMPTY_OR_NULL_MESSAGE)
    @NotBlank(message = EMPTY_OR_NULL_MESSAGE)
    @Email(message = INVALID_EMAIL_MESSAGE)
    private String email;

    @NotNull(message = EMPTY_OR_NULL_MESSAGE)
    @NotEmpty(message = EMPTY_OR_NULL_MESSAGE)
    @NotBlank(message = EMPTY_OR_NULL_MESSAGE)
    @Min(value = 8, message = INVALID_PASSWORD_MESSAGE)
    private String password;
}
