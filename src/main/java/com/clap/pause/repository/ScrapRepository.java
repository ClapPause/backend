package com.clap.pause.repository;

import com.clap.pause.model.Member;
import com.clap.pause.model.Post;
import com.clap.pause.model.Scrap;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScrapRepository extends JpaRepository<Scrap, Long> {
    Optional<Scrap> findByPostAndMember(Post post, Member member);

    boolean existsByPostAndMember(Post post, Member member);
}
