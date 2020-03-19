package com.project.postex.service;

import com.project.postex.models.Account;
import com.project.postex.models.User;
import com.project.postex.models.dto.AuthResponseDTO;
import com.project.postex.repository.AccountRepository;
import com.project.postex.security.JwtTokenProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.*;

@Service
@AllArgsConstructor
public class AuthenticationService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final Validator validator;
    private final ValidationErrorFormatter errorFormatter;

    public Mono<AuthResponseDTO> signUp(Mono<Account> accountMono) {
        return accountMono
                .doOnNext(account -> {
                    var errors = new BeanPropertyBindingResult(account, "account");
                    validator.validate(account, errors);
                    if (errors.hasErrors()) {
                        throw new ResponseStatusException(BAD_REQUEST, errorFormatter.format(errors));
                    }
                }).map(account -> {
                    var user = account.getUser();
                    user.setPassword(passwordEncoder.encode(user.getPassword()));
                    return account;
                }).flatMap(account -> accountRepository
                        .findByUserUsernameIgnoreCase(account.getUser().getUsername())
                        .flatMap(__ -> Mono.error(new ResponseStatusException(CONFLICT, "User already exists!")))
                        .switchIfEmpty(accountRepository.save(account))
                        .cast(Account.class))
                .map(this::toResponse);
    }

    public Mono<AuthResponseDTO> signIn(Mono<User> userMono) {
        return userMono
                .flatMap(user -> accountRepository
                        .findByUserUsername(user.getUsername())
                        .switchIfEmpty(Mono.error(new ResponseStatusException(NOT_FOUND, "User not found!")))
                        .flatMap(accountFromDb ->
                                passwordEncoder.matches(user.getPassword(), accountFromDb.getUser().getPassword())
                                        ? Mono.just(toResponse(accountFromDb))
                                        : Mono.error(new ResponseStatusException(CONFLICT, "Incorrect password!"))));
    }

    private AuthResponseDTO toResponse(Account account) {
        return new AuthResponseDTO("Bearer " + tokenProvider.generateToken(account.getUser()), account);
    }
}
