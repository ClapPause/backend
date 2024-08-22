package com.clap.pause.service;

import com.clap.pause.dto.memberUniversityDepartment.CertificateResponse;
import com.clap.pause.model.DepartmentType;
import com.clap.pause.model.Member;
import com.clap.pause.model.MemberUniversityDepartment;
import com.clap.pause.model.UniversityDepartment;
import com.clap.pause.repository.MemberUniversityDepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberUniversityDepartmentService {
    private final MemberUniversityDepartmentRepository memberUniversityDepartmentRepository;

    @Transactional
    public CertificateResponse certificateMember(Member member, UniversityDepartment universityDepartment,
                                                 DepartmentType departmentType) {
        var memberUniversityDepartment = memberUniversityDepartmentRepository.save(
                new MemberUniversityDepartment(member, universityDepartment, departmentType));

        return new CertificateResponse(memberUniversityDepartment.getId(),
                memberUniversityDepartment.getMember().getId(),
                memberUniversityDepartment.getUniversityDepartment().getId(),
                memberUniversityDepartment.getDepartmentType());
    }
}
