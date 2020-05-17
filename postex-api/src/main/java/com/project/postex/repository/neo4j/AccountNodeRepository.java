package com.project.postex.repository.neo4j;

import com.project.postex.models.neo4j.AccountNode;
import org.neo4j.springframework.data.repository.ReactiveNeo4jRepository;
import org.neo4j.springframework.data.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface AccountNodeRepository extends ReactiveNeo4jRepository<AccountNode, String> {
    @Query("MATCH(a:Account {id: $accountId})-[s:SUBSCRIBED_ON]->(f:Account) WHERE f.id = $friendId DELETE s")
    Mono<Void> detachFriend(@Param("accountId") String accountId, @Param("friendId") String friendId);
}
