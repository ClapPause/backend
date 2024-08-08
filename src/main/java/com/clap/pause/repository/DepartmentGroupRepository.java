package com.clap.pause.repository;

import com.clap.pause.model.DepartmentGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentGroupRepository extends JpaRepository<DepartmentGroup, Long> {
}
