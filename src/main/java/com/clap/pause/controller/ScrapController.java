package com.clap.pause.controller;

import com.clap.pause.dto.scrap.ScrapResponse;
import com.clap.pause.service.ScrapService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/posts/{postId}/scrap")
public class ScrapController {
    private final ScrapService scrapService;

    @PostMapping
    public ResponseEntity<Void> scrapPost(@PathVariable(name = "postId") Long postId) {
        scrapService.scarpPost(postId, getMemberId());
        return ResponseEntity.created(URI.create("api/posts/" + postId + "/scrap"))
                .build();
    }

    @GetMapping
    public ResponseEntity<ScrapResponse> getScrap(@PathVariable(name = "postId") Long postId) {
        var response = scrapService.getScrap(postId, getMemberId());
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteScrap(@PathVariable(name = "postId") Long postId) {
        scrapService.deleteScrap(postId, getMemberId());
        return ResponseEntity.noContent().build();
    }

    private Long getMemberId() {
        var authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        return (Long) authentication.getPrincipal();
    }
}
