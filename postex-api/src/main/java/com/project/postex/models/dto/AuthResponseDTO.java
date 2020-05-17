package com.project.postex.models.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.project.postex.models.mongo.Account;
import com.project.postex.models.views.Views;
import lombok.Value;

@Value
public class AuthResponseDTO {
    @JsonView(Views.Info.class) String token;
    @JsonView(Views.Info.class) Account account;
}
