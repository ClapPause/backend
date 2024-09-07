package com.clap.pause.repository;

import com.clap.pause.model.ImageVoteOption;
import com.clap.pause.model.Post;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageVoteOptionRepository extends JpaRepository<ImageVoteOption, Long> {
    Optional<List<ImageVoteOption>> findByPost(Post post);
}
