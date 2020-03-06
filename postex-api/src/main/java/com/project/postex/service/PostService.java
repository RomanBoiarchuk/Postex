package com.project.postex.service;

import com.project.postex.models.Post;
import com.project.postex.repository.AccountRepository;
import com.project.postex.repository.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final AccountRepository accountRepository;

    public Mono<Post> createPost(Mono<Post> postMono, Mono<String> username) {
        return postMono
                .zipWith(username.flatMap(accountRepository::findByUserUsername))
                .flatMap(tuple -> {
                    var post = tuple.getT1();
                    var author = tuple.getT2();
                    post.setAuthor(author);
                    return postRepository.save(post);
                });
    }

    public Mono<Post> updatePost(Mono<Post> postMono, String id) {
        return postRepository
                .findById(id)
                .zipWith(postMono)
                .flatMap(tuple -> {
                    var postFromDb = tuple.getT1();
                    var post = tuple.getT2();
                    postFromDb.update(post);
                    return postRepository.save(postFromDb);
                });
    }

    public Flux<Post> getPosts() {
        return postRepository.findAll();
    }

    public Mono<Void> deletePostById(String id) {
        return postRepository.deleteById(id);
    }
}
