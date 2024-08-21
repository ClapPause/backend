package com.clap.pause.repository;

import com.clap.pause.model.UniversityDepartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UniversityDepartmentRepository extends JpaRepository<UniversityDepartment, Long> {
}
