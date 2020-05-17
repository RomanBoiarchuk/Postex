package com.project.postex.models.mongo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.project.postex.models.views.Views;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

@Document
@Data
public class Account {
    @JsonView(Views.Info.class)
    private String id;
    @NotBlank(message = "First name can't be empty.")
    @Size(min = 1, max = 20, message = "First name is too long.")
    @JsonView(Views.Info.class)
    private String firstName;
    @NotBlank(message = "Last name can't be empty.")
    @Size(min = 1, max = 20, message = "Last name is too long.")
    @JsonView(Views.Info.class)
    private String lastName;
    @Size(max = 35, message = "Country name is too long.")
    @JsonView(Views.Info.class)
    private String country;
    @Size(max = 35, message = "City name is too long.")
    @JsonView(Views.Info.class)
    private String city;
    private String about;
    @CreatedDate
    @JsonView(Views.Info.class)
    private Date signUpDate;
    @Valid
    @JsonView(Views.Info.class)
    @NotNull(message = "User can't be null.")
    private User user;
    @DBRef(lazy = true)
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private List<Account> friends = new ArrayList<>();
    @DBRef(lazy = true)
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private List<Account> subscribers = new ArrayList<>();
    @Transient
    @EqualsAndHashCode.Exclude
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer connectionDegree;

    @Transient
    @JsonProperty(value = "friendsCount", access = READ_ONLY)
    public int getFriendsCount() {
        return friends.size();
    }

    @Transient
    @JsonProperty(value = "subscribersCount", access = READ_ONLY)
    public int getSubscribersCount() {
        return subscribers.size();
    }
}
