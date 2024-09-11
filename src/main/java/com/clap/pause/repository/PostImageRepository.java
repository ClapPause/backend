package com.clap.pause.repository;

import com.clap.pause.model.Post;
import com.clap.pause.model.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {
    void deleteAllByPost(Post post);
}
