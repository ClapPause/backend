package com.clap.pause.repository;

import com.clap.pause.model.ImageVoteOption;
import com.clap.pause.model.Post;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageVoteOptionRepository extends JpaRepository<ImageVoteOption, Long> {
    List<ImageVoteOption> findAllByPost(Post post);
}
