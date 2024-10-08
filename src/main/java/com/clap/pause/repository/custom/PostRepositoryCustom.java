package com.clap.pause.repository.custom;

import com.clap.pause.model.Post;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepositoryCustom {
    public List<Post> getTop3HotPostByLike();
}
