package com.clap.pause.controller;

import com.clap.pause.dto.comment.CommentRequest;
import com.clap.pause.dto.comment.CommentResponse;
import com.clap.pause.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Void> saveComment(@PathVariable Long postId,
                                            @Valid @RequestBody CommentRequest commentRequest) {
        var memberId = getMemberId();
        commentService.saveComment(memberId, postId, commentRequest);
        return ResponseEntity.ok()
                .build();
    }

    @PostMapping("/{parentCommentId}")
    public ResponseEntity<Void> saveReply(@PathVariable Long postId,
                                          @PathVariable Long parentCommentId,
                                          @Valid @RequestBody CommentRequest commentRequest) {
        var memberId = getMemberId();
        commentService.saveReply(memberId, postId, parentCommentId, commentRequest);
        return ResponseEntity.ok()
                .build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateComment(@PathVariable Long postId,
                                              @PathVariable Long id,
                                              @Valid @RequestBody CommentRequest commentRequest) {
        commentService.updateComment(id, postId, commentRequest);
        return ResponseEntity.noContent()
                .build();
    }

    @GetMapping
    public ResponseEntity<List<CommentResponse>> getComments(@PathVariable Long postId) {
        var comments = commentService.getComments(postId);
        return ResponseEntity.ok(comments);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long postId,
                                              @PathVariable Long id) {
        commentService.deleteComment(postId, id);
        return ResponseEntity.noContent()
                .build();
    }

    private Long getMemberId() {
        var authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        return (Long) authentication.getPrincipal();
    }
}
