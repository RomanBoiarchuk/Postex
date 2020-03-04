package com.project.postex.models;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Document
@Data
public class Post {
    private String id;
    @NotBlank(message = "The text of a post can't be empty.")
    private String text;
    @NotBlank(message = "Tags can't be empty.")
    private List<String> tags;
    @DBRef
    private Account author;
    private List<Comment> comments;
    private List<String> likeAccountIds;
    @CreatedDate
    private LocalDateTime creationTime;
    @LastModifiedDate
    private LocalDateTime lastModifiedTime;
}
