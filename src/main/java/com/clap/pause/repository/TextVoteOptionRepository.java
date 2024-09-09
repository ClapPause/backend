package com.clap.pause.repository;

import com.clap.pause.model.Post;
import com.clap.pause.model.TextVoteOption;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TextVoteOptionRepository extends JpaRepository<TextVoteOption, Long> {
    Optional<List<TextVoteOption>> findAllByPost(Post post);
}
