package com.clap.pause.controller;

import com.clap.pause.service.ImageVoteHistoryService;
import com.clap.pause.service.TextVoteHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/vote/{postId}")
public class VoteController {
    private final ImageVoteHistoryService imageVoteHistoryService;
    private final TextVoteHistoryService textVoteHistoryService;

//    @PostMapping
//    public ResponseEntity<Void> vote(@Valid @RequestBody VoteRequest voteRequest)
}
