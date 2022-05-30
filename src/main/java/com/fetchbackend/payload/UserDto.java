package com.fetchbackend.payload;

import lombok.Data;

import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class UserDto {
    @NotEmpty
    private String username;
    @NotEmpty
    @Size(min = 5, message = "Password should be at least 5 characters long")
    private String password;
}
