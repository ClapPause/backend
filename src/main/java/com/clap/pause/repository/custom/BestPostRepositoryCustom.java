package com.clap.pause.repository.custom;

import com.clap.pause.model.Post;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BestPostRepositoryCustom {
    List<Post> getBestPostsByCreatedAt();

    List<Post> getBestPostsByLike();

    Post getBestPostByCreatedAtDesc();
}
