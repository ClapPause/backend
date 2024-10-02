package com.clap.pause.controller;

import com.clap.pause.service.BestPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/best")
public class BestPostController {
    private final BestPostService bestPostService;

}
