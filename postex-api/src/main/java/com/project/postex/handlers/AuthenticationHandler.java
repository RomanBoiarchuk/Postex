package com.project.postex.handlers;

import com.project.postex.models.Account;
import com.project.postex.models.User;
import com.project.postex.models.views.Views;
import com.project.postex.service.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.codec.json.Jackson2CodecSupport.JSON_VIEW_HINT;

@Component
@AllArgsConstructor
public class AuthenticationHandler {
    private final AuthenticationService authService;

    public Mono<ServerResponse> signUp(ServerRequest request) {
        return authService
                .signUp(request.bodyToMono(Account.class))
                .flatMap(responseDTO -> ServerResponse.ok()
                        .hint(JSON_VIEW_HINT, Views.Info.class)
                        .bodyValue(responseDTO));
    }

    public Mono<ServerResponse> signIn(ServerRequest request) {
        return authService
                .signIn(request.bodyToMono(User.class))
                .flatMap(responseDTO -> ServerResponse.ok()
                        .hint(JSON_VIEW_HINT, Views.Info.class)
                        .bodyValue(responseDTO))
                .onErrorResume(IllegalArgumentException.class, e ->
                        ServerResponse.badRequest().bodyValue(e.getMessage()));
    }
}
