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

import java.util.List;

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

    public MemberUniversityDepartmentResponse getMemberUniversityDepartment(Long id) {
        var memberUniversityDepartment = memberUniversityDepartmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundElementException(id + "를 가진 이용자의 학과정보가 존재하지 않습니다."));
        return getMemberUniversityDepartmentResponse(memberUniversityDepartment);
    }

    public List<MemberUniversityDepartmentResponse> getMemberUniversityDepartments(Long memberId) {
        var memberUniversityDepartments = memberUniversityDepartmentRepository.findAllByMemberId(memberId);
        return memberUniversityDepartments.stream()
                .map(this::getMemberUniversityDepartmentResponse)
                .toList();
    }

    public void deleteMemberUniversityDepartment(Long memberUniversityDepartmentId) {
        if (!memberUniversityDepartmentRepository.existsById(memberUniversityDepartmentId)) {
            throw new NotFoundElementException("존재하지 않는 이용자의 학과 정보 입니다.");
        }
        memberUniversityDepartmentRepository.deleteById(memberUniversityDepartmentId);
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
