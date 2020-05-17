package com.project.postex.repository.mongo;

import com.project.postex.models.mongo.Post;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;

@Repository
public interface PostRepository extends ReactiveMongoRepository<Post, String> {
    String addLikesAndCommentsCount = "{ $addFields: {" +
            "likesCount: {$size: '$likeAccountIds'}, " +
            "commentsCount: {$size: '$comments'} } }";
    String matchAuthorId = "{ $match : { 'author.id' : :#{#authorId} } }";
    String matchAuthorIdIn = "{ $match : { 'author.$id' : { $in : :#{#authorIds} } } }";
    String matchContainsTag = "{ $match : { 'tags' : { '$eq' : :#{#tag} } } }";

    @Aggregation(addLikesAndCommentsCount)
    <T> Flux<T> findAll(Class<T> type, Sort sort);

    <T> Mono<T> findById(String id, Class<T> type);

    @Aggregation(pipeline = {matchAuthorId, addLikesAndCommentsCount})
    <T> Flux<T> findByAuthorId(String authorId, Sort sort, Class<T> type);

    @Aggregation(pipeline = {matchAuthorIdIn, addLikesAndCommentsCount})
    <T> Flux<T> findByAuthorIdIn(Collection<ObjectId> authorIds, Sort sort, Class<T> type);

    @Aggregation(pipeline = {matchContainsTag, addLikesAndCommentsCount})
    <T> Flux<T> findByTagsContaining(String tag, Sort sort, Class<T> type);
}
