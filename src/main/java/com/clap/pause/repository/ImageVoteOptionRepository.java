package com.clap.pause.repository;

import com.clap.pause.model.ImageVoteOption;
import com.clap.pause.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageVoteOptionRepository extends JpaRepository<ImageVoteOption, Long> {
    List<ImageVoteOption> findAllByPost(Post post);
}
