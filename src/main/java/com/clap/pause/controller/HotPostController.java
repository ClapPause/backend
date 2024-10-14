package com.clap.pause.controller;

import com.clap.pause.dto.post.response.PostListResponse;
import com.clap.pause.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/hot")
public class HotPostController {
    private final PostService postService;

    @Scheduled(cron = "0 0 10 * * ?")
    @GetMapping
    public ResponseEntity<List<PostListResponse>> registerHotPost() {
        List<PostListResponse> responses = postService.registerHotPost();
        return ResponseEntity.ok().body(responses);
    }

    @GetMapping("/sort")
    public ResponseEntity<List<PostListResponse>> getHotPosts(@RequestParam(name = "sortType") String sortType) {
        var responses = postService.getHotposts(sortType);
        return ResponseEntity.ok().body(responses);
    }
}
