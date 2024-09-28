package com.clap.pause.repository;

import com.clap.pause.model.UniversityDepartment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UniversityDepartmentRepository extends JpaRepository<UniversityDepartment, Long> {
    List<UniversityDepartment> findAllByUniversityContains(String university);

    List<UniversityDepartment> findAllByUniversityContainsAndDepartmentContains(String university, String department);
}
