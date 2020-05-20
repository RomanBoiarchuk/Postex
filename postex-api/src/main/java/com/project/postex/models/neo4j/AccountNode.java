package com.project.postex.models.neo4j;

import lombok.Data;
import org.neo4j.springframework.data.core.schema.Id;
import org.neo4j.springframework.data.core.schema.Node;
import org.neo4j.springframework.data.core.schema.Property;
import org.neo4j.springframework.data.core.schema.Relationship;

import java.util.ArrayList;
import java.util.List;

import static org.neo4j.springframework.data.core.schema.Relationship.Direction.INCOMING;

@Node("Account")
@Data
public class AccountNode {
    @Id
    private String id;
    @Property("title")
    private String username;
    @Relationship(type = "SUBSCRIBED_ON")
    private List<AccountNode> friends = new ArrayList<>();
    @Relationship(type = "HAS_SUBSCRIBER", direction = INCOMING)
    private List<AccountNode> subscribers = new ArrayList<>();
}
