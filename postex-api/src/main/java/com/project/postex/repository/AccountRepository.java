package com.project.postex.repository;

import com.project.postex.models.Account;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface AccountRepository extends ReactiveMongoRepository<Account, String> {
    Mono<Account> findByUserUsernameIgnoreCase(String username);

    Mono<Account> findByUserUsername(String username);

    @SuppressWarnings("SpringDataRepositoryMethodReturnTypeInspection")
    Mono<Boolean> existsByUserUsername(String username);
}
