package com.clap.pause.controller;

import com.clap.pause.dto.postlike.PostLikeResponse;
import com.clap.pause.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postId}/like")
public class PostLikeController {
    private final PostLikeService postLikeService;

    @PostMapping()
    public ResponseEntity<Void> like(@PathVariable(name = "postId") Long postId) {
        postLikeService.like(postId, getMemberId());
        return ResponseEntity.created(URI.create("/api/post-like/" + postId))
                .build();
    }

    @GetMapping()
    public ResponseEntity<PostLikeResponse> getLike(@PathVariable(name = "postId") Long postId) {
        PostLikeResponse response = postLikeService.getLike(postId, getMemberId());
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping()
    public ResponseEntity<Void> dislike(@PathVariable(name = "postId") Long postId) {
        postLikeService.dislike(postId, getMemberId());
        return ResponseEntity.noContent().build();
    }

    private Long getMemberId() {
        var authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        return (Long) authentication.getPrincipal();
    }
}
