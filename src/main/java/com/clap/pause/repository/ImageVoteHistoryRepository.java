package com.clap.pause.repository;

import com.clap.pause.model.ImageVoteHistory;
import com.clap.pause.model.ImageVoteOption;
import com.clap.pause.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageVoteHistoryRepository extends JpaRepository<ImageVoteHistory, Long> {
    List<ImageVoteHistory> findAllByMember(Member member);

    Long countByImageVoteOption(ImageVoteOption imageVoteOption);

    boolean existsByMember(Member member);
}
