package com.project.postex.service;

import com.project.postex.models.Account;
import com.project.postex.models.Comment;
import com.project.postex.models.Post;
import com.project.postex.repository.AccountRepository;
import com.project.postex.repository.PostRepository;
import com.project.postex.repository.projections.CommentsOnly;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.NOT_FOUND;

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

    public Mono<Void> setLike(Mono<String> accountIdMono, String postId) {
        return postRepository
                .findById(postId)
                .zipWith(accountIdMono)
                .flatMap(tuple -> {
                    var post = tuple.getT1();
                    var accountId = tuple.getT2();
                    post.getLikeAccountIds().add(accountId);
                    return postRepository.save(post);
                }).then(Mono.empty());
    }

    public Mono<Void> removeLike(Mono<String> accountIdMono, String postId) {
        return postRepository
                .findById(postId)
                .zipWith(accountIdMono)
                .flatMap(tuple -> {
                    var post = tuple.getT1();
                    var accountId = tuple.getT2();
                    post.getLikeAccountIds().remove(accountId);
                    return postRepository.save(post);
                }).then(Mono.empty());
    }

    public Mono<Post> createComment(Mono<Comment> commentMono, Mono<Account> authorMono, String postId) {
        return postRepository
                .findById(postId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(NOT_FOUND, "Post not found.")))
                .zipWith(commentMono)
                .flatMap(tuple ->
                        authorMono
                                .flatMap(author -> {
                                    var post = tuple.getT1();
                                    var comment = tuple.getT2();
                                    comment.setId(ObjectId.get().toString());
                                    comment.setCreationTime(LocalDateTime.now());
                                    comment.setAuthor(author);
                                    post.getComments().add(comment);
                                    return postRepository.save(post);
                                }));
    }

    public Flux<Comment> findCommentsByPostId(String postId) {
        return postRepository
                .findById(postId, CommentsOnly.class)
                .switchIfEmpty(Mono.error(new ResponseStatusException(NOT_FOUND, "Post not found.")))
                .map(CommentsOnly::getComments)
                .flatMapMany(Flux::fromIterable);
    }
}
