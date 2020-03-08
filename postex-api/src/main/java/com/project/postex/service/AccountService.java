package com.project.postex.service;

import com.project.postex.models.Account;
import com.project.postex.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@AllArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    public Mono<Account> findByUsername(Mono<String> monoUsername) {
        return monoUsername
                .flatMap(accountRepository::findByUserUsername)
                .switchIfEmpty(Mono.error(new ResponseStatusException(NOT_FOUND, "User not found!")));
    }
}
