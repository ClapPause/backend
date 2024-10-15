package com.clap.pause.service;

import com.clap.pause.dto.post.response.PostListResponse;
import com.clap.pause.exception.BestPostFailException;
import com.clap.pause.model.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service

@RequiredArgsConstructor
public class BestPostFacadeService {
    private final PostService postService;
    private final BestPostService bestPostService;

    @Transactional(readOnly = true)
    public List<PostListResponse> getBestPosts(String sortType) {
        if (sortType.equals("popularity")) {
            List<Post> postList = bestPostService.getBestPostByPopularity();
            return postList.stream()
                    .map(post -> postService.getPost(post.getId()))
                    .toList();
        }
        if (sortType.equals("recent")) {
            var postList = bestPostService.getBestPostByRecent();
            return postList.stream()
                    .map(post -> postService.getPost(post.getId()))
                    .toList();
        }
        throw new BestPostFailException("해당 정렬기준은 존재하지 않습니다. 다시 시도해주세요");
    }

    @Transactional(readOnly = true)
    public PostListResponse getNewestBestPost() {
        var post = bestPostService.getNewestBestPost();
        return postService.getPost(post.getId());
    }

}
