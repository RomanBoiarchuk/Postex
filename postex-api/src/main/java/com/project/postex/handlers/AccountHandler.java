package com.project.postex.handlers;

import com.project.postex.models.Account;
import com.project.postex.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.security.Principal;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
@AllArgsConstructor
public class AccountHandler {

    private final AccountService accountService;

    @PreAuthorize("isAuthenticated()")
    public Mono<ServerResponse> getProfile(ServerRequest request) {
        Mono<String> username = request.principal().map(Principal::getName);
        Mono<Account> accountMono = accountService.findByUsername(username);
        return accountMono
                .flatMap(account -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .body(BodyInserters.fromValue(account)));
    }
}
