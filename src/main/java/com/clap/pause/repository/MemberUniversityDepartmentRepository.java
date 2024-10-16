package com.clap.pause.repository;

import com.clap.pause.model.MemberUniversityDepartment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberUniversityDepartmentRepository extends JpaRepository<MemberUniversityDepartment, Long> {
    List<MemberUniversityDepartment> findAllByMemberId(Long memberId);

    void deleteAllByMemberId(Long memberId);
}
