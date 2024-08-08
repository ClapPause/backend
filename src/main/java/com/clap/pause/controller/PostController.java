package com.clap.pause.controller;

import com.clap.pause.dto.post.PostRequest;
import com.clap.pause.dto.post.PostResponse;
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
    public ResponseEntity<PostResponse> saveDefaultPost(@RequestAttribute("memberId") Long memberId, @Valid @RequestBody PostRequest postRequest) {
        var post = postService.saveDefaultPost(memberId, postRequest);
        return ResponseEntity.created(URI.create("/api/post/default/" + post.id())).body(post);
    }
}
