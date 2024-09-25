package com.clap.pause.repository;

import com.clap.pause.model.Post;
import com.clap.pause.model.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {
    void deleteAllByPost(Post post);

    List<PostImage> findAllByPost(Post post);
}
