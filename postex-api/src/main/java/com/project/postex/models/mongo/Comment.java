package com.project.postex.models.mongo;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class Comment {
    private String id;
    @DBRef
    private Account author;
    @NotBlank(message = "The text of a comment can't be empty.")
    private String text;
    @CreatedDate
    private LocalDateTime creationTime;
    private List<Comment> replies = new ArrayList<>();
}
