package com.clap.pause.repository;

import com.clap.pause.model.DepartmentGroup;
import com.clap.pause.model.Post;
import com.clap.pause.repository.custom.PostRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
    List<Post> findByDepartmentGroupOrderByCreatedAtDesc(DepartmentGroup departmentGroup);
}
