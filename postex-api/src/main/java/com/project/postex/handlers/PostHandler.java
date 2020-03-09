package com.project.postex.handlers;

import com.project.postex.models.Account;
import com.project.postex.models.Comment;
import com.project.postex.models.Post;
import com.project.postex.repository.projections.PostInfo;
import com.project.postex.service.AccountService;
import com.project.postex.service.AuthorizationService;
import com.project.postex.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
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

    @PreAuthorize("isAuthenticated()")
    public Mono<ServerResponse> getPosts(ServerRequest request) {
        var posts = request.principal().map(Principal::getName)
                .flatMapMany(postService::findPostsByUsername);
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON).body(posts, PostInfo.class);
    }

    public Mono<ServerResponse> getPost(ServerRequest request) {
        Mono<Post> post = postService.findPostById(request.pathVariable("id"));
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(post, Post.class);
    }

    public Mono<ServerResponse> getPostsByAuthor(ServerRequest request) {
        var authorId = request.pathVariable("id");
        Flux<PostInfo> posts = postService.getPostsByAuthorId(authorId);
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(posts, PostInfo.class);
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
    public Mono<ServerResponse> deletePost(ServerRequest request) {
        var response = postService.deletePostById(request.pathVariable("id"))
                .then(ServerResponse.noContent().build());
        return handleSecurity(request, response);
    }

    @PreAuthorize("isAuthenticated()")
    public Mono<ServerResponse> setLike(ServerRequest request) {
        Mono<String> accountId = getAccount(request).map(Account::getId);
        return postService
                .setLike(accountId, request.pathVariable("id"))
                .then(ServerResponse.noContent().build());
    }

    @PreAuthorize("isAuthenticated()")
    public Mono<ServerResponse> removeLike(ServerRequest request) {
        Mono<String> accountId = getAccount(request).map(Account::getId);
        return postService
                .removeLike(accountId, request.pathVariable("id"))
                .then(ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> getCommentsByPostId(ServerRequest request) {
        var postId = request.pathVariable("postId");
        Flux<Comment> comments = postService.findCommentsByPostId(postId);
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(comments, Comment.class);
    }

    @PreAuthorize("isAuthenticated()")
    public Mono<ServerResponse> createComment(ServerRequest request) {
        Mono<Account> account = getAccount(request);
        Mono<Comment> comment = request.bodyToMono(Comment.class);
        return postService
                .createComment(comment, account, request.pathVariable("postId"))
                .flatMap(post -> ServerResponse
                        .created(URI.create(String.format("/posts/%s", request.pathVariable("postId"))))
                        .contentType(APPLICATION_JSON)
                        .bodyValue(post));
    }

    private Mono<Account> getAccount(ServerRequest request) {
        return request
                .principal()
                .map(Principal::getName)
                .flatMap(username -> accountService.findByUsername(Mono.just(username)));
    }

    private Mono<ServerResponse> handleSecurity(ServerRequest request, Mono<ServerResponse> responseMono) {
        return authorizationService
                .mayUpdatePost(request.pathVariable("id"))
                .flatMap(hasPermission -> hasPermission ? responseMono
                        : Mono.error(new AuthorizationServiceException("You don't have permission to change this post.")))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
