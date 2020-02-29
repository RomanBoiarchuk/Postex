package com.project.postex.models;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Document
@Data
public class Account {
    private String id;
    @NotNull(message = "First name can't be empty.")
    @Size(min = 1, max = 20, message = "First name size must be between 1 and 20 characters.")
    private String firstName;
    @NotNull(message = "Last name can't be empty.")
    @Size(min = 1, max = 20, message = "Last name size must be between 1 and 20 characters.")
    private String lastName;
    @NotNull(message = "User can't be null.")
    private User user;
}
