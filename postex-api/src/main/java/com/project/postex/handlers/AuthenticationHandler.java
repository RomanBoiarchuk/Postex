package com.project.postex.handlers;

import com.project.postex.models.Account;
import com.project.postex.models.User;
import com.project.postex.service.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.validation.ValidationException;

@Component
@AllArgsConstructor
public class AuthenticationHandler {
    private final AuthenticationService authService;

    public Mono<ServerResponse> signUp(ServerRequest request) {
        return authService
                .signUp(request.bodyToMono(Account.class))
                .flatMap(token -> ServerResponse.ok()
                        .body(BodyInserters.fromValue(token)))
                .onErrorResume(IllegalArgumentException.class, e ->
                        ServerResponse.badRequest().bodyValue(e.getMessage()))
                .onErrorResume(ValidationException.class, e ->
                        ServerResponse.badRequest().bodyValue(e.getMessage()));
    }

    public Mono<ServerResponse> signIn(ServerRequest request) {
        return authService
                .signIn(request.bodyToMono(User.class))
                .flatMap(token -> ServerResponse.ok()
                        .body(BodyInserters.fromValue(token)))
                .onErrorResume(IllegalArgumentException.class, e ->
                        ServerResponse.badRequest().bodyValue(e.getMessage()));
    }
}
