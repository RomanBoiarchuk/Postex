package com.project.postex.service;

import com.project.postex.models.Account;
import com.project.postex.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    public Mono<Account> findByUsername(Mono<String> monoUsername) {
        return monoUsername
                .flatMap(accountRepository::findByUserUsername)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("User not found!")));
    }
}
