package com.project.postex.models;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document
@Data
public class Account {
    private String id;
    @NotBlank(message = "First name can't be empty.")
    @Size(min = 1, max = 20, message = "First name is too long.")
    private String firstName;
    @NotBlank(message = "Last name can't be empty.")
    @Size(min = 1, max = 20, message = "Last name is too long.")
    private String lastName;
    @Size(max = 35, message = "Country name is too long.")
    private String country;
    @Size(max = 35, message = "City name is too long.")
    private String city;
    private String about;
    @CreatedDate
    private Date signUpDate;
    @Valid
    @NotNull(message = "User can't be null.")
    private User user;
    @DBRef(lazy = true)
    private List<Account> friends = new ArrayList<>();
}
