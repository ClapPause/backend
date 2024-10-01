package com.clap.pause.controller;

import com.clap.pause.dto.post.request.ImageVoteRequest;
import com.clap.pause.dto.post.request.PostRequest;
import com.clap.pause.dto.post.request.TextVoteRequest;
import com.clap.pause.dto.post.response.PostListResponse;
import com.clap.pause.service.PostService;
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

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/department-groups/{departmentGroupId}/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<Void> saveDefaultPost(@PathVariable(name = "departmentGroupId") Long departmentGroupId, @Valid @RequestBody PostRequest postRequest) {
        var memberId = getMemberId();
        var post = postService.saveDefaultPost(memberId, postRequest, departmentGroupId);
        return ResponseEntity.created(URI.create("/api/department-groups/" + departmentGroupId + "/posts" + post.id()))
                .build();
    }

    @PostMapping("/text-vote")
    public ResponseEntity<Void> saveTextVote(@PathVariable(name = "departmentGroupId") Long departmentGroupId, @Valid @RequestBody TextVoteRequest textVoteRequest) {
        var memberId = getMemberId();
        var post = postService.saveTextVote(memberId, textVoteRequest, departmentGroupId);
        return ResponseEntity.created(URI.create("/api/department-groups/" + departmentGroupId + "/posts" + post.id()))
                .build();
    }

    @PostMapping("/image-vote")
    public ResponseEntity<Void> saveImageVote(@PathVariable(name = "departmentGroupId") Long departmentGroupId, @Valid @RequestBody ImageVoteRequest imageVoteRequest) {
        var memberId = getMemberId();
        var post = postService.saveImageVote(memberId, imageVoteRequest, departmentGroupId);
        return ResponseEntity.created(URI.create("/api/department-groups/" + departmentGroupId + "/posts" + post.id()))
                .build();
    }

    @GetMapping
    public ResponseEntity<List<PostListResponse>> getAllPosts(@PathVariable(name = "departmentGroupId") Long departmentGroupId) {
        var postListRespons = postService.getAllPosts(departmentGroupId);
        return ResponseEntity.ok().body(postListRespons);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostListResponse> getPost(@PathVariable(name = "departmentGroupId") Long departmentGroupId, @PathVariable(name = "postId") Long postId) {
        var response = postService.getPost(postId);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<Void> updatePost(@PathVariable(name = "departmentGroupId") Long departmentGroupId, @PathVariable(name = "postId") Long postId, @Valid @RequestBody PostRequest postRequest) {
        postService.updatePost(postId, postRequest);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/text-vote/{postId}")
    public ResponseEntity<Void> updateTextVote(@PathVariable(name = "departmentGroupId") Long departmentGroupId, @PathVariable(name = "postId") Long postId, @Valid @RequestBody TextVoteRequest textVoteRequest) {
        postService.updateTextVote(postId, textVoteRequest);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/image-vote/{postId}")
    public ResponseEntity<Void> updateImageVote(@PathVariable(name = "departmentGroupId") Long departmentGroupId, @PathVariable(name = "postId") Long postId, @Valid @RequestBody ImageVoteRequest imageVoteRequest) {
        postService.updateImageVote(postId, imageVoteRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable(name = "departmentGroupId") Long departmentGroupId, @PathVariable(name = "postId") Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }

    private Long getMemberId() {
        var authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        return (Long) authentication.getPrincipal();
    }
}
