package com.clap.pause.repository;

import com.clap.pause.model.Member;
import com.clap.pause.model.TextVoteHistory;
import com.clap.pause.model.TextVoteOption;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TextVoteHistoryRepository extends JpaRepository<TextVoteHistory, Long> {
    List<TextVoteHistory> findAllByMember(Member member);

    Long countByTextVoteOption(TextVoteOption textVoteOption);

    boolean existsByMember(Member member);
}
