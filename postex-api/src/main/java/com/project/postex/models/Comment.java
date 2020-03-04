package com.project.postex.models;

import org.springframework.data.mongodb.core.mapping.DBRef;

import javax.validation.constraints.NotBlank;
import java.util.List;

public class Comment {
    private String id;
    @DBRef
    private Account author;
    @NotBlank(message = "The text of a comment can't be empty.")
    private String text;
    private List<Comment> replies;
}
