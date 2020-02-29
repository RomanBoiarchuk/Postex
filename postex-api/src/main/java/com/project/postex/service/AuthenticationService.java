package com.project.postex.service;

import com.project.postex.models.Account;
import com.project.postex.models.User;
import com.project.postex.repository.AccountRepository;
import com.project.postex.security.JwtTokenProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Validator;
import reactor.core.publisher.Mono;

import javax.validation.ValidationException;

@Service
@AllArgsConstructor
public class AuthenticationService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final Validator validator;

    public Mono<String> signUp(Mono<Account> accountMono) {
        return accountMono
                .map(account -> {
                    var user = account.getUser();
                    user.setPassword(passwordEncoder.encode(user.getPassword()));
                    return account;
                }).doOnNext(account -> {
                    var errors = new BeanPropertyBindingResult(account, "account");
                    validator.validate(account, errors);
                    if (errors.hasErrors()) {
                        throw new ValidationException(errors.toString());
                    }
                }).flatMap(account -> accountRepository
                        .findByUserUsernameIgnoreCase(account.getUser().getUsername())
                        .flatMap(__ -> Mono.error(new IllegalArgumentException("User already exists!")))
                        .switchIfEmpty(accountRepository.save(account))
                        .cast(Account.class))
                .map(savedAccount -> "Bearer " + tokenProvider.generateToken(savedAccount.getUser()));
    }

    public Mono<String> signIn(Mono<User> userMono) {
        return userMono
                .flatMap(user -> accountRepository
                        .findByUserUsername(user.getUsername())
                        .switchIfEmpty(Mono.error(new IllegalArgumentException("User not found!")))
                        .flatMap(accountFromDb ->
                                passwordEncoder.matches(user.getPassword(), accountFromDb.getUser().getPassword())
                                        ? Mono.just("Bearer " + tokenProvider.generateToken(accountFromDb.getUser()))
                                        : Mono.error(new IllegalArgumentException("Incorrect password!"))));
    }
}
