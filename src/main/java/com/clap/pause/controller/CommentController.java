package com.clap.pause.controller;

import com.clap.pause.dto.comment.CommentRequest;
import com.clap.pause.dto.comment.ReplyRequest;
import com.clap.pause.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Void> saveComment(@PathVariable Long postId,
                                            @Valid @RequestBody CommentRequest commentRequest) {
        var memberId = getMemberId();
        var comment = commentService.saveComment(memberId, postId, commentRequest);
        var location = String.format("/api/posts/%s/comments/%s", postId, comment.id());
        return ResponseEntity.created(URI.create(location))
                .build();
    }

    @PostMapping("/{parentCommentId}")
    public ResponseEntity<Void> saveReply(@PathVariable Long postId,
                                          @PathVariable Long parentCommentId,
                                          @Valid @RequestBody ReplyRequest replyRequest) {
        var memberId = getMemberId();
        var comment = commentService.saveReply(memberId, postId, parentCommentId, replyRequest);
        var location = String.format("/api/posts/%s/comments/%s", postId, comment.id());
        return ResponseEntity.created(URI.create(location))
                .build();
    }

    @PostMapping("/{commentId}")
    public ResponseEntity<Void> updateComment(@PathVariable Long postId,
                                              @PathVariable Long commentId,
                                              @Valid @RequestBody CommentRequest commentRequest) {
        commentService.updateComment(commentId, postId, commentRequest);
        return ResponseEntity.noContent()
                .build();
    }

    private Long getMemberId() {
        var authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        return (Long) authentication.getPrincipal();
    }
}
