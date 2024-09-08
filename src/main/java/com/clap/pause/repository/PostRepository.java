package com.clap.pause.repository;

import com.clap.pause.model.DepartmentGroup;
import com.clap.pause.model.Post;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByDepartmentGroupOrderByCreatedAtDesc(DepartmentGroup departmentGroup);
}
