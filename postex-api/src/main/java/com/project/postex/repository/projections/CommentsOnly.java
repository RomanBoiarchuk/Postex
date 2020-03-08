package com.project.postex.repository.projections;

import com.project.postex.models.Comment;
import lombok.Value;

import java.util.List;

@Value
public class CommentsOnly {
    List<Comment> comments;
}
