package com.project.postex.repository.projections;

import com.project.postex.models.Account;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Value
public class PostInfo {
    String id;
    List<String> tags;
    Account author;
    String text;
    Set<String> likeAccountIds;
    int likesCount;
    int commentsCount;
    LocalDateTime creationTime;
}
