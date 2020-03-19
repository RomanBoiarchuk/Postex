package com.project.postex.service;

import com.project.postex.models.Account;
import com.project.postex.repository.AccountRepository;
import com.project.postex.repository.projections.FriendsOnly;
import com.project.postex.repository.projections.SubscribersOnly;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@AllArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public Mono<Account> findById(String accountId) {
        return accountRepository
                .findById(accountId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(NOT_FOUND, "User not found!")));
    }

    public Mono<Account> findByUsername(Mono<String> monoUsername) {
        return monoUsername
                .flatMap(accountRepository::findByUserUsername)
                .switchIfEmpty(Mono.error(new ResponseStatusException(NOT_FOUND, "User not found!")));
    }

    public Flux<Account> searchAccounts(String search) {
        return accountRepository.search(search);
    }

    public Flux<Account> findFriendsById(String accountId) {
        return accountRepository
                .findById(accountId, FriendsOnly.class)
                .switchIfEmpty(Mono.error(new ResponseStatusException(NOT_FOUND, "User not found!")))
                .map(FriendsOnly::getFriends)
                .flatMapMany(Flux::fromIterable);
    }

    public Flux<Account> findSubscribersById(String accountId) {
        return accountRepository
                .findById(accountId, SubscribersOnly.class)
                .switchIfEmpty(Mono.error(new ResponseStatusException(NOT_FOUND, "User not found!")))
                .map(SubscribersOnly::getSubscribers)
                .flatMapMany(Flux::fromIterable);
    }

    public Mono<Void> addFriendById(Mono<Account> accountMono, String friendId) {
        return accountMono
                .zipWith(accountRepository
                        .findById(friendId)
                        .switchIfEmpty(Mono.error(new ResponseStatusException(NOT_FOUND, "User not found!")))
                ).flatMap(tuple -> {
                    var account = tuple.getT1();
                    var friend = tuple.getT2();
                    if (!account.equals(friend) && !account.getFriends().contains(friend)) {
                        account.getFriends().add(friend);
                        friend.getSubscribers().add(account);
                    }
                    return accountRepository.saveAll(Arrays.asList(account, friend)).collectList();
                }).then();
    }

    public Mono<Void> removeFriendById(Mono<Account> accountMono, String friendId) {
        return accountMono
                .zipWith(accountRepository
                        .findById(friendId)
                        .switchIfEmpty(Mono.error(new ResponseStatusException(NOT_FOUND, "User not found!")))
                ).flatMap(tuple -> {
                    var account = tuple.getT1();
                    var friend = tuple.getT2();
                    account.getFriends().remove(friend);
                    friend.getSubscribers().remove(account);
                    return accountRepository.saveAll(Arrays.asList(account, friend)).collectList();
                }).then();
    }

    public Mono<Boolean> isFriend(String username, String friendId) {
        return accountRepository
                .findByUserUsername(username)
                .map(account -> account.getFriends().stream()
                        .anyMatch(acc -> acc.getId().equals(friendId)));
    }
}
