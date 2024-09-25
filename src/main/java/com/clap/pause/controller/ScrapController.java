package com.clap.pause.controller;

import com.clap.pause.service.ScrapService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts/{postId}/scrap")
public class ScrapController {
    private final ScrapService scrapService;
}
