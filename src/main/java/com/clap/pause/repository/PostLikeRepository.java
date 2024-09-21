package com.clap.pause.repository;

import com.clap.pause.model.Member;
import com.clap.pause.model.Post;
import com.clap.pause.model.PostLike;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    boolean existsByMemberAndPost(Member member, Post post);

    int countByPost(Post post);

    Optional<PostLike> findByPostAndMember(Post post, Member member);
}
