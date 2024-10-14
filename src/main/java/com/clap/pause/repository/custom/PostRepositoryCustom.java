package com.clap.pause.repository.custom;

import com.clap.pause.model.Post;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepositoryCustom {
    List<Post> getTop3HotPostByLike();

    List<Post> getHotPostsByCreatedAt();

    List<Post> getHotPostsByLike();
}
