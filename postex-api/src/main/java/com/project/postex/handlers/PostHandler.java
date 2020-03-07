package com.project.postex.handlers;

import com.project.postex.models.Account;
import com.project.postex.models.Post;
import com.project.postex.service.AccountService;
import com.project.postex.service.AuthorizationService;
import com.project.postex.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.security.Principal;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
@AllArgsConstructor
public class PostHandler {
    private final PostService postService;
    private final AuthorizationService authorizationService;
    private final AccountService accountService;

    public Mono<ServerResponse> getPosts(ServerRequest request) {
        return postService.getPosts()
                .collectList()
                .flatMap(posts -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON).bodyValue(posts));
    }

    @PreAuthorize("isAuthenticated()")
    public Mono<ServerResponse> createPost(ServerRequest request) {
        Mono<String> username = request.principal().map(Principal::getName);
        Mono<Post> createdPost = postService.createPost(request.bodyToMono(Post.class), username);
        return createdPost
                .flatMap(post -> ServerResponse.created(URI.create("/posts/" + post.getId()))
                        .contentType(APPLICATION_JSON).bodyValue(post));
    }

    @PreAuthorize("isAuthenticated()")
    public Mono<ServerResponse> updatePost(ServerRequest request) {
        Mono<Post> postMono = request.bodyToMono(Post.class);
        Mono<Post> updatedPost = postService.updatePost(postMono, request.pathVariable("id"));
        var response = updatedPost
                .flatMap(post -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON).bodyValue(post));
        return handleSecurity(request, response);
    }

    @PreAuthorize("isAuthenticated()")
    public Mono<ServerResponse> setLike(ServerRequest request) {
        Mono<String> accountId = getAccountId(request);
        return postService
                .setLike(accountId, request.pathVariable("id"))
                .then(ServerResponse.noContent().build());
    }

    @PreAuthorize("isAuthenticated()")
    public Mono<ServerResponse> removeLike(ServerRequest request) {
        Mono<String> accountId = getAccountId(request);
        return postService
                .removeLike(accountId, request.pathVariable("id"))
                .then(ServerResponse.noContent().build());
    }

    @PreAuthorize("isAuthenticated()")
    public Mono<ServerResponse> deletePost(ServerRequest request) {
        var response = postService.deletePostById(request.pathVariable("id"))
                .then(ServerResponse.noContent().build());
        return handleSecurity(request, response);
    }

    private Mono<String> getAccountId(ServerRequest request) {
        return request
                .principal()
                .map(Principal::getName)
                .flatMap(username -> accountService.findByUsername(Mono.just(username)))
                .map(Account::getId);
    }

    private Mono<ServerResponse> handleSecurity(ServerRequest request, Mono<ServerResponse> responseMono) {
        return authorizationService
                .mayUpdatePost(request.pathVariable("id"))
                .flatMap(hasPermission -> hasPermission ? responseMono
                        : Mono.error(new AuthorizationServiceException("You don't have permission to change this post.")))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
