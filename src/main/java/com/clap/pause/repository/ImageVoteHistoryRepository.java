package com.clap.pause.repository;

import com.clap.pause.model.ImageVoteHistory;
import com.clap.pause.model.ImageVoteOption;
import com.clap.pause.model.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageVoteHistoryRepository extends JpaRepository<ImageVoteHistory, Long> {
    List<ImageVoteHistory> findAllByMember(Member member);

    Long countByImageVoteOption(ImageVoteOption imageVoteOption);

    boolean existsByMember(Member member);
}
