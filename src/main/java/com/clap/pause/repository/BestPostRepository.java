package com.clap.pause.repository;

import com.clap.pause.model.BestPost;
import com.clap.pause.repository.custom.BestPostRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BestPostRepository extends JpaRepository<BestPost, Long>, BestPostRepositoryCustom {
}
