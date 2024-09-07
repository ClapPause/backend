package com.clap.pause.controller;

import com.clap.pause.dto.post.request.ImageVoteRequest;
import com.clap.pause.dto.post.request.PostRequest;
import com.clap.pause.dto.post.request.TextVoteRequest;
import com.clap.pause.dto.post.response.PostListResponse;
import com.clap.pause.exception.PostAccessException;
import com.clap.pause.service.PostService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/department-groups/{departmentgroupId}/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<Void> saveDefaultPost(@PathVariable(name = "departmentgroupId") Long departmentgroupId, @Valid @RequestBody PostRequest postRequest, @RequestPart(value = "image", required = false) List<MultipartFile> imageFiles) {
        var memberId = getMemberId();
        var post = postService.saveDefaultPost(memberId, postRequest, departmentgroupId, imageFiles);
        return ResponseEntity.created(URI.create("/api/departmentgroups/" + departmentgroupId + "/posts" + post.id()))
                .build();
    }

    @PostMapping
    public ResponseEntity<Void> saveTextVote(@PathVariable(name = "departmentgroupId") Long departmentgroupId, @Valid @RequestBody TextVoteRequest textVoteRequest, @RequestPart(value = "image", required = false) MultipartFile imageFile) {
        var memberId = getMemberId();
        var post = postService.saveTextVote(memberId, textVoteRequest, departmentgroupId, imageFile);
        return ResponseEntity.created(URI.create("/api/departmentgroups/" + departmentgroupId + "/posts" + post.id()))
                .build();
    }

    @PostMapping
    public ResponseEntity<Void> saveImageVote(@PathVariable(name = "departmentgroupId") Long departmentgroupId, @Valid @RequestBody ImageVoteRequest imageVoteRequest, @RequestPart(value = "image") List<MultipartFile> imageFiles) {
        var memberId = getMemberId();
        var post = postService.saveImageVote(memberId, imageVoteRequest, departmentgroupId, imageFiles);
        return ResponseEntity.created(URI.create("/api/departmentgroups/" + departmentgroupId + "/posts" + post.id()))
                .build();
    }


    @GetMapping
    public ResponseEntity<List<PostListResponse>> getAllPosts(@PathVariable(name = "departmentgroupId") Long departmentGroupId) throws PostAccessException {
        var postListRespons = postService.getAllPosts(departmentGroupId);
        return ResponseEntity.ok().body(postListRespons);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostListResponse> getPost(@PathVariable(name = "departmentgroupId") Long departmentGroupId, @PathVariable(name = "postId") Long postId) throws PostAccessException {
        var response = postService.getPostResponse(postId);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<Void> updatePost(@PathVariable(name = "departmentgroupId") Long departmentGroupId, @PathVariable(name = "postId") Long postId, @Valid @RequestBody PostRequest postRequest) {
        postService.updatePost(postId, postRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable(name = "departmentgroupId") Long departmentGroupId, @PathVariable(name = "postId") Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }

    private Long getMemberId() {
        var authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        return (Long) authentication.getPrincipal();
    }
}
