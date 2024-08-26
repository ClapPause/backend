package com.clap.pause.controller;

import com.clap.pause.dto.post.request.PostRequest;
import com.clap.pause.dto.post.response.PostListResponse;
import com.clap.pause.exception.PostAccessException;
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
@RequestMapping("/api/department-groups/{departmentgroupId}/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final com.clap.pause.service.getPostsService getPostsService;

    @PostMapping
    public ResponseEntity<Void> saveDefaultPost(
            @PathVariable(name = "departmentgroupId") Long departmentgroupId,
            @Valid @RequestBody PostRequest postRequest) {
        var memberId = getMemberId();
        var post = postService.saveDefaultPost(memberId, postRequest, departmentgroupId);
        return ResponseEntity.created(URI.create("/api/departmentgroups/" + post.departmentGroupId()
                        + "/posts" + post.id()))
                .build();
    }

    @GetMapping
    public ResponseEntity<List<PostListResponse>> getAllPosts(
            @PathVariable(name = "departmentgroupId") Long departmentGroupId) throws PostAccessException {
        List<PostListResponse> postResponses = getPostsService.getAllPosts(departmentGroupId);
        return ResponseEntity.ok().body(postResponses);
    }

    private Long getMemberId() {
        var authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        return (Long) authentication.getPrincipal();
    }
}
