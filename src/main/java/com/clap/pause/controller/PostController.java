package com.clap.pause.controller;

import com.clap.pause.dto.post.request.PostRequest;
import com.clap.pause.dto.post.response.PostListResponse;
import com.clap.pause.dto.post.response.PostResponse;
import com.clap.pause.service.PostService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/departmentgroups/{departmentgroupId}/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    /**
     * 기본 게시글을 생성
     *
     * @param postRequest
     * @return postResponse
     */
    @PostMapping
    public ResponseEntity<PostResponse> saveDefaultPost(@Valid @RequestBody PostRequest postRequest) {
        var memberId = getMemberId();
        var post = postService.saveDefaultPost(memberId, postRequest);
        return ResponseEntity.created(URI.create("/api/departmentgroups/" + post.departmentGroupId()
                        + "/posts" + post.id()))
                .body(post);
    }

    /**
     * 기본 게시글을 불러옴
     *
     * @param departmentId
     * @return List<PostListResponse>
     */
    @GetMapping
    public ResponseEntity<List<PostListResponse>> getAllPosts(@PathVariable("departmentgroupId") Long departmentId) {
        var posts = postService.getAllPosts(departmentId);
        return ResponseEntity.ok().body(posts);

    }

    /**
     * Header의 Authorization 으로 memberId를 return함
     *
     * @return
     */
    private Long getMemberId() {
        var authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        return (Long) authentication.getPrincipal();
    }
}
