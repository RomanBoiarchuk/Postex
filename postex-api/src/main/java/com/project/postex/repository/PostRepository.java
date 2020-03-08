package com.project.postex.repository;

import com.project.postex.models.Post;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface PostRepository extends ReactiveMongoRepository<Post, String> {
    <T> Mono<T> findById(String id, Class<T> type);
}
