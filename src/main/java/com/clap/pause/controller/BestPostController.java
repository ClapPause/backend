package com.clap.pause.controller;

import com.clap.pause.dto.post.response.PostListResponse;
import com.clap.pause.service.BestPostFacadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/best")
public class BestPostController {
    private final BestPostFacadeService bestPostFacadeService;

    @GetMapping("/sort")
    public ResponseEntity<List<PostListResponse>> getBestPosts(@RequestParam(name = "sortType") String sortType) {
        //sortType이 popularity면 인기순, recent면 최신순으로 정렬된다
        var responses = bestPostFacadeService.getBestPosts(sortType);
        return ResponseEntity.ok().body(responses);
    }

    @GetMapping
    public ResponseEntity<PostListResponse> getNewestBestPost() {
        var response = bestPostFacadeService.getNewestBestPost();
        return ResponseEntity.ok().body(response);
    }
}
