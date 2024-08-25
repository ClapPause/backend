package com.clap.pause.service;

import com.clap.pause.dto.memberUniversityDepartment.MemberUniversityDepartmentRequest;
import com.clap.pause.dto.memberUniversityDepartment.MemberUniversityDepartmentResponse;
import com.clap.pause.exception.NotFoundElementException;
import com.clap.pause.model.MemberUniversityDepartment;
import com.clap.pause.repository.MemberRepository;
import com.clap.pause.repository.MemberUniversityDepartmentRepository;
import com.clap.pause.repository.UniversityDepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberUniversityDepartmentService {

    private final MemberUniversityDepartmentRepository memberUniversityDepartmentRepository;
    private final MemberRepository memberRepository;
    private final UniversityDepartmentRepository universityDepartmentRepository;

    public MemberUniversityDepartmentResponse saveMemberUniversityDepartment(Long memberId, MemberUniversityDepartmentRequest memberUniversityDepartmentRequest) {
        var memberUniversityDepartment = saveMemberUniversityDepartmentWithMemberUniversityDepartmentRequest(memberId, memberUniversityDepartmentRequest);
        return getMemberUniversityDepartmentResponse(memberUniversityDepartment);
    }

    private MemberUniversityDepartment saveMemberUniversityDepartmentWithMemberUniversityDepartmentRequest(Long memberId, MemberUniversityDepartmentRequest memberUniversityDepartmentRequest) {
        var member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundElementException(memberId + "를 가진 이용자가 존재하지 않습니다."));
        var universityDepartment = universityDepartmentRepository.findById(memberUniversityDepartmentRequest.universityDepartmentId())
                .orElseThrow(() -> new NotFoundElementException(memberUniversityDepartmentRequest.universityDepartmentId() + "를 가진 대학교의 학과가 존재하지 않습니다."));
        var memberUniversityDepartment = new MemberUniversityDepartment(member, universityDepartment, memberUniversityDepartmentRequest.departmentType());
        return memberUniversityDepartmentRepository.save(memberUniversityDepartment);
    }

    private MemberUniversityDepartmentResponse getMemberUniversityDepartmentResponse(MemberUniversityDepartment memberUniversityDepartment) {
        return MemberUniversityDepartmentResponse.of(memberUniversityDepartment.getId(), memberUniversityDepartment.getUniversityDepartment().getUniversity(), memberUniversityDepartment.getUniversityDepartment().getDepartment(), memberUniversityDepartment.getDepartmentType());
    }
}
