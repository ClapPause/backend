package com.clap.pause.controller;

import com.clap.pause.dto.post.request.ImageVoteRequest;
import com.clap.pause.dto.post.request.PostRequest;
import com.clap.pause.dto.post.request.TextVoteRequest;
import com.clap.pause.dto.post.response.PostListResponse;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/department-groups/{departmentGroupId}/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<Void> saveDefaultPost(@PathVariable(name = "departmentGroupId") Long departmentGroupId, @Valid @RequestPart(value = "request") PostRequest postRequest, @RequestPart(value = "image", required = false) List<MultipartFile> imageFiles) {
        var memberId = getMemberId();
        var post = postService.saveDefaultPost(memberId, postRequest, departmentGroupId, imageFiles);
        return ResponseEntity.created(URI.create("/api/department-groups/" + departmentGroupId + "/posts" + post.id()))
                .build();
    }

    @PostMapping("/textvote")
    public ResponseEntity<Void> saveTextVote(@PathVariable(name = "departmentGroupId") Long departmentGroupId, @Valid @RequestPart(value = "request") TextVoteRequest textVoteRequest, @RequestPart(value = "image", required = false) MultipartFile imageFile) {
        var memberId = getMemberId();
        var post = postService.saveTextVote(memberId, textVoteRequest, departmentGroupId, imageFile);
        return ResponseEntity.created(URI.create("/api/department-groups/" + departmentGroupId + "/posts" + post.id()))
                .build();
    }

    @PostMapping("/imageVote")
    public ResponseEntity<Void> saveImageVote(@PathVariable(name = "departmentGroupId") Long departmentGroupId, @Valid @RequestPart(value = "request") ImageVoteRequest imageVoteRequest, @RequestPart(value = "image") List<MultipartFile> imageFiles) {
        var memberId = getMemberId();
        var post = postService.saveImageVote(memberId, imageVoteRequest, departmentGroupId, imageFiles);
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
    public ResponseEntity<Void> updatePost(@PathVariable(name = "departmentGroupId") Long departmentGroupId, @PathVariable(name = "postId") Long postId, @Valid @RequestPart(value = "request") PostRequest postRequest, @RequestPart(value = "image", required = false) List<MultipartFile> imageFiles) {
        postService.updatePost(postId, postRequest, imageFiles);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/textvote/{postId}")
    public ResponseEntity<Void> updateTextVote(@PathVariable(name = "departmentGroupId") Long departmentGroupId, @PathVariable(name = "postId") Long postId, @Valid @RequestPart(value = "request") TextVoteRequest textVoteRequest, @RequestPart(value = "image", required = false) MultipartFile imageFile) {
        postService.updateTextVote(postId, textVoteRequest, imageFile);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/imageVote/{postId}")
    public ResponseEntity<Void> updateImageVote(@PathVariable(name = "departmentGroupId") Long departmentGroupId, @PathVariable(name = "postId") Long postId, @Valid @RequestPart(value = "request") ImageVoteRequest imageVoteRequest, @RequestPart(value = "image") List<MultipartFile> imageFiles) {
        postService.updateImageVote(postId, imageVoteRequest, imageFiles);
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
