package com.clap.pause.controller;

import com.clap.pause.service.PostLikeService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post-like")
public class PostLikeController {
    private final PostLikeService postLikeService;

    @PostMapping("/{postId}")
    public ResponseEntity<Void> like(@PathVariable(name = "postId") Long postId) {
        postLikeService.like(postId, getMemberId());
        return ResponseEntity.created(URI.create("/api/post-like/" + postId))
                .build();
    }

    private Long getMemberId() {
        var authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        return (Long) authentication.getPrincipal();
    }
}
