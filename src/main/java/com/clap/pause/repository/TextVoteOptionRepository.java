package com.clap.pause.repository;

import com.clap.pause.model.Post;
import com.clap.pause.model.TextVoteOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TextVoteOptionRepository extends JpaRepository<TextVoteOption, Long> {
    List<TextVoteOption> findAllByPost(Post post);
}
