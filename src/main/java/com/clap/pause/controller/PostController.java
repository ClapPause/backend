package com.clap.pause.controller;

import com.clap.pause.dto.post.DefaultPostRequest;
import com.clap.pause.dto.post.DefaultPostResponse;
import com.clap.pause.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/default")
    public ResponseEntity<DefaultPostResponse> saveDefaultPost(@RequestAttribute("memberId") Long memberId, @Valid @RequestBody DefaultPostRequest defaultPostRequest) {
        var post = postService.saveDefaultPost(memberId, defaultPostRequest);
        return ResponseEntity.created(URI.create("/api/post/default/" + post.id())).body(post);
    }
}
