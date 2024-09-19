package com.clap.pause.controller;

import com.clap.pause.dto.vote.VoteRequest;
import com.clap.pause.dto.vote.VoteResponse;
import com.clap.pause.service.VoteService;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/votes")
public class VoteController {
    private final VoteService voteService;

    @PostMapping
    public ResponseEntity<Void> voteOption(@Valid @RequestBody VoteRequest voteRequest) {
        voteService.voteOption(voteRequest, getMemberId());
        return ResponseEntity.created(URI.create("/api/votes/" + voteRequest.postId()))
                .build();
    }

    @PutMapping
    public ResponseEntity<Void> revoteOption(@Valid @RequestBody VoteRequest voteRequest) {
        voteService.revoteOption(voteRequest, getMemberId());
        return ResponseEntity.created(URI.create("/api/votes/" + voteRequest.postId()))
                .build();
    }

    @GetMapping("/{postId}")
    public ResponseEntity<VoteResponse> getVoteResult(@PathVariable(name = "postId") Long postId) {
        var response = voteService.getVoteResult(postId);
        return ResponseEntity.ok().body(response);
    }

    private Long getMemberId() {
        var authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        return (Long) authentication.getPrincipal();
    }
}
