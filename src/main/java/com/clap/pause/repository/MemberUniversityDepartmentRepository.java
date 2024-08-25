package com.clap.pause.repository;

import com.clap.pause.model.MemberUniversityDepartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberUniversityDepartmentRepository extends JpaRepository<MemberUniversityDepartment, Long> {
}
