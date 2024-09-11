package com.clap.pause.repository;

import com.clap.pause.model.TextVoteHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TextVoteHistoryRepository extends JpaRepository<TextVoteHistory, Long> {
}
