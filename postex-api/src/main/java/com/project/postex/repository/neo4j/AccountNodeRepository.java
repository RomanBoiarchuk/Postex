package com.project.postex.repository.neo4j;

import com.project.postex.models.neo4j.AccountNode;
import org.neo4j.springframework.data.repository.ReactiveNeo4jRepository;
import org.neo4j.springframework.data.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface AccountNodeRepository extends ReactiveNeo4jRepository<AccountNode, String> {
    @Query("MATCH(a:Account {id: $accountId})-[s:SUBSCRIBED_ON]->(f:Account) WHERE f.id = $friendId DELETE s")
    Mono<Void> detachFriend(@Param("accountId") String accountId, @Param("friendId") String friendId);

    @Query("MATCH p=shortestPath(" +
            "(a1:Account {id: $account1Id})-[SUBSCRIBED_ON*..5]->(a2:Account {id: $account2Id})" +
            ") RETURN length(p)")
    Mono<Integer> findShortestPathLength(@Param("account1Id") String account1Id, @Param("account2Id") String account2Id);

    @Query("MATCH (a:Account {id: $accountId})-[:SUBSCRIBED_ON]->(friends:Account)-[:SUBSCRIBED_ON]-" +
            "(potentialFriends:Account) WHERE NOT (a)-[:SUBSCRIBED_ON]->(potentialFriends) " +
            "and a <> potentialFriends RETURN DISTINCT potentialFriends.id as Recommended")
    Flux<String> findFriendsRecommendations(@Param("accountId") String accountId);
}
