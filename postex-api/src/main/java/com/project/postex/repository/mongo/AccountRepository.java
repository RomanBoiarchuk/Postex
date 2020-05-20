package com.project.postex.repository.mongo;

import com.project.postex.models.mongo.Account;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface AccountRepository extends ReactiveMongoRepository<Account, String> {

    <T> Mono<T> findById(String id, Class<T> type);

    Mono<Account> findByUserUsernameIgnoreCase(String username);

    Mono<Account> findByUserUsername(String username);

    Flux<Account> findByUserUsernameContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            String username, String firstName, String lastName);

    default Flux<Account> search(String search) {
        return findByUserUsernameContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                search, search, search);
    }

    @SuppressWarnings("SpringDataRepositoryMethodReturnTypeInspection")
    Mono<Boolean> existsByUserUsername(String username);
}
