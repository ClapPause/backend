package com.clap.pause.repository;

import com.clap.pause.model.Member;
import com.clap.pause.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Optional<Notification> findByMemberId(Long memberId);

    boolean existsByMember(Member member);

    void deleteByMemberId(Long memberId);
}
