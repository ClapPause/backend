package com.clap.pause.repository;

import com.clap.pause.model.Post;
import com.clap.pause.model.PostImage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {

    void deleteAllByPostId(Long postId);

    List<PostImage> findAllByPost(Post post);
}
