package com.project.postex.service;

import com.project.postex.models.dto.AuthResponseDTO;
import com.project.postex.models.mongo.Account;
import com.project.postex.models.mongo.User;
import com.project.postex.models.neo4j.AccountNode;
import com.project.postex.repository.mongo.AccountRepository;
import com.project.postex.repository.neo4j.AccountNodeRepository;
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
    private final AccountNodeRepository accountNodeRepository;
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
                        .cast(Account.class)
                        .doOnNext(savedAccount -> {
                            var accountNode = new AccountNode();
                            accountNode.setId(savedAccount.getId());
                            accountNode.setUsername(savedAccount.getUser().getUsername());
                            accountNodeRepository.save(accountNode).subscribe();
                        })
                ).map(this::toResponse);
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
