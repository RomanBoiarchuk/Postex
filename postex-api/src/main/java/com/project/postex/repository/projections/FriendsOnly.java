package com.project.postex.repository.projections;

import com.project.postex.models.Account;
import lombok.Value;

import java.util.List;

@Value
public class FriendsOnly {
    List<Account> friends;
}