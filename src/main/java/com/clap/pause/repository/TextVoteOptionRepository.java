package com.clap.pause.repository;

import com.clap.pause.model.Post;
import com.clap.pause.model.TextVoteOption;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TextVoteOptionRepository extends JpaRepository<TextVoteOption, Long> {
    List<TextVoteOption> findAllByPost(Post post);
}
