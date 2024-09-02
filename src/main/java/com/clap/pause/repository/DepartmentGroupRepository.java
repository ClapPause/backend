package com.clap.pause.repository;

import com.clap.pause.model.DepartmentGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartmentGroupRepository extends JpaRepository<DepartmentGroup, Long> {
    Optional<DepartmentGroup> findByName(String name);

    boolean existsByName(String name);
}
