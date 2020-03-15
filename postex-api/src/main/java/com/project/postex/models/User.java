package com.project.postex.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.project.postex.models.views.Views;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Data
@JsonIgnoreProperties(value = "password", allowSetters = true)
public class User {
    @NotNull(message = "Username can't be empty.")
    @Size(min = 1, max = 20, message = "Username size must be between 1 and 20 characters.")
    @JsonView(Views.Info.class)
    private String username;
    @NotNull(message = "Password can't be empty.")
    @Size(min = 8, max = 25, message = "Password size must be between 8 and 25 characters.")
    private String password;
}
