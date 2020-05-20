package com.project.postex.service;

import com.project.postex.repository.mongo.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class AuthorizationService {
    private final PostRepository postRepository;

    public Mono<Boolean> mayUpdatePost(String postId) {
        return postRepository
                .findById(postId)
                .zipWith(ReactiveSecurityContextHolder
                        .getContext().map(SecurityContext::getAuthentication))
                .map(tuple -> {
                    var post = tuple.getT1();
                    var username = tuple.getT2().getName();
                    return post.getAuthor().getUser().getUsername().equals(username);
                });
    }
}
