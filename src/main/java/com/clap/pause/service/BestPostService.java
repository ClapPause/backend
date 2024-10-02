package com.clap.pause.service;

import com.clap.pause.model.Post;
import com.clap.pause.repository.BestPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BestPostService {
    private final BestPostRepository bestPostRepository;

    @Transactional(readOnly = true)
    public List<Post> getBestPostByRecent() {
        return bestPostRepository.getBestPostsByCreatedAt();
    }

    @Transactional(readOnly = true)
    public List<Post> getBestPostByPopularity() {
        return bestPostRepository.getBestPostsByLike();
    }

    @Transactional(readOnly = true)
    public Post getNewestBestPost() {
        return bestPostRepository.getBestPostByCreatedAtDesc();
    }
}
