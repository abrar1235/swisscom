package com.swisscom.operations.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

import static com.swisscom.operations.constant.Messages.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private String id;

    @NotNull(message = EMPTY_OR_NULL_MESSAGE)
    @NotEmpty(message = EMPTY_OR_NULL_MESSAGE)
    @NotBlank(message = EMPTY_OR_NULL_MESSAGE)
    private String name;

    @NotNull(message = EMPTY_OR_NULL_MESSAGE)
    @NotEmpty(message = EMPTY_OR_NULL_MESSAGE)
    @NotBlank(message = EMPTY_OR_NULL_MESSAGE)
    private String roles;

    @NotNull(message = EMPTY_OR_NULL_MESSAGE)
    @NotEmpty(message = EMPTY_OR_NULL_MESSAGE)
    @NotBlank(message = EMPTY_OR_NULL_MESSAGE)
    @Min(value = 8, message = INVALID_PASSWORD_MESSAGE)
    private String password;

    @NotNull(message = EMPTY_OR_NULL_MESSAGE)
    @NotEmpty(message = EMPTY_OR_NULL_MESSAGE)
    @NotBlank(message = EMPTY_OR_NULL_MESSAGE)
    @Email(message = INVALID_EMAIL_MESSAGE)
    private String email;

    private String status;
    private String token;
}
