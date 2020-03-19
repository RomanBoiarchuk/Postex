package com.project.postex.handlers;

import com.project.postex.models.Account;
import com.project.postex.models.views.Views;
import com.project.postex.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.Principal;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.codec.json.Jackson2CodecSupport.JSON_VIEW_HINT;

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

    public Mono<ServerResponse> searchProfiles(ServerRequest request) {
        var search = request.queryParam("search").orElse("");
        Flux<Account> accounts = accountService.searchAccounts(search);
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .hint(JSON_VIEW_HINT, Views.Info.class)
                .body(accounts, Account.class);
    }

    public Mono<ServerResponse> getProfileById(ServerRequest request) {
        var id = request.pathVariable("id");
        return accountService.findById(id)
                .flatMap(account ->
                        ServerResponse.ok()
                                .body(BodyInserters.fromValue(account)));
    }

    public Mono<ServerResponse> getFriends(ServerRequest request) {
        var id = request.pathVariable("id");
        Flux<Account> friends = accountService.findFriendsById(id);
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .hint(JSON_VIEW_HINT, Views.Info.class)
                .body(friends, Account.class);
    }

    public Mono<ServerResponse> getSubscribers(ServerRequest request) {
        var id = request.pathVariable("id");
        Flux<Account> subscribers = accountService.findSubscribersById(id);
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .hint(JSON_VIEW_HINT, Views.Info.class)
                .body(subscribers, Account.class);
    }

    @PreAuthorize("isAuthenticated()")
    public Mono<ServerResponse> checkIfMyFriend(ServerRequest request) {
        var friendId = request.pathVariable("id");
        Mono<Boolean> isFriend = request.principal()
                .map(Principal::getName)
                .flatMap(username -> accountService.isFriend(username, friendId));
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(isFriend, Boolean.class);
    }

    @PreAuthorize("isAuthenticated()")
    public Mono<ServerResponse> addFriend(ServerRequest request) {
        Mono<String> username = request.principal().map(Principal::getName);
        var friendId = request.pathVariable("id");
        Mono<Account> account = accountService.findByUsername(username);
        return accountService
                .addFriendById(account, friendId)
                .then(ServerResponse.noContent().build());
    }

    @PreAuthorize("isAuthenticated()")
    public Mono<ServerResponse> removeFriend(ServerRequest request) {
        Mono<String> username = request.principal().map(Principal::getName);
        var friendId = request.pathVariable("id");
        Mono<Account> account = accountService.findByUsername(username);
        return accountService
                .removeFriendById(account, friendId)
                .then(ServerResponse.noContent().build());
    }
}
