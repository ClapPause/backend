package com.clap.pause.service;

import com.clap.pause.dto.departmentGroup.DepartmentGroupResponse;
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

    /**
     * 이용자의 학과 정보를 저장하는 메서드
     *
     * @param memberId
     * @param memberUniversityDepartmentRequest
     * @return memberUniversityDepartment
     */
    public MemberUniversityDepartmentResponse saveMemberUniversityDepartment(Long memberId, MemberUniversityDepartmentRequest memberUniversityDepartmentRequest) {
        var memberUniversityDepartment = saveMemberUniversityDepartmentWithMemberUniversityDepartmentRequest(memberId, memberUniversityDepartmentRequest);
        return getMemberUniversityDepartmentResponse(memberUniversityDepartment);
    }

    /**
     * 이용자의 학과 정보를 id 를 사용하여 조회하는 메서드
     *
     * @param id
     * @return memberUniversityDepartmentResponse
     */
    public MemberUniversityDepartmentResponse getMemberUniversityDepartment(Long id) {
        var memberUniversityDepartment = memberUniversityDepartmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundElementException(id + "를 가진 이용자의 학과정보가 존재하지 않습니다."));
        return getMemberUniversityDepartmentResponse(memberUniversityDepartment);
    }

    /**
     * memberId 를 사용하여 이용자의 전체 학과 정보를 조회하는 메서드
     *
     * @param memberId
     * @return memberUniversityDepartments
     */
    public List<MemberUniversityDepartmentResponse> getMemberUniversityDepartments(Long memberId) {
        var memberUniversityDepartments = memberUniversityDepartmentRepository.findAllByMemberId(memberId);
        return memberUniversityDepartments.stream()
                .map(this::getMemberUniversityDepartmentResponse)
                .toList();
    }

    /**
     * 이용자의 학과정보 ID 를 사용해서 삭제하는 메서드
     *
     * @param id
     */
    public void deleteMemberUniversityDepartment(Long id) {
        if (!memberUniversityDepartmentRepository.existsById(id)) {
            throw new NotFoundElementException("존재하지 않는 이용자의 학과 정보 입니다.");
        }
        memberUniversityDepartmentRepository.deleteById(id);
    }

    /**
     * 이용자의 학과 정보를 memberId 와 RequestDTO 를 사용하여 저장하는 메서드
     *
     * @param memberId
     * @param memberUniversityDepartmentRequest
     * @return memberUniversityDepartment
     */
    private MemberUniversityDepartment saveMemberUniversityDepartmentWithMemberUniversityDepartmentRequest(Long memberId, MemberUniversityDepartmentRequest memberUniversityDepartmentRequest) {
        var member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundElementException(memberId + "를 가진 이용자가 존재하지 않습니다."));
        var universityDepartment = universityDepartmentRepository.findById(memberUniversityDepartmentRequest.universityDepartmentId())
                .orElseThrow(() -> new NotFoundElementException(memberUniversityDepartmentRequest.universityDepartmentId() + "를 가진 대학교의 학과가 존재하지 않습니다."));
        var memberUniversityDepartment = new MemberUniversityDepartment(member, universityDepartment, memberUniversityDepartmentRequest.departmentType());
        return memberUniversityDepartmentRepository.save(memberUniversityDepartment);
    }

    /**
     * 이용자의 학과 정보 엔티티를 ResponseDTO 로 변환하는 메서드
     *
     * @param memberUniversityDepartment
     * @return memberUniversityDepartmentResponse
     */
    private MemberUniversityDepartmentResponse getMemberUniversityDepartmentResponse(MemberUniversityDepartment memberUniversityDepartment) {
        var departmentGroupResponse = new DepartmentGroupResponse(memberUniversityDepartment.getUniversityDepartment().getDepartmentGroup().getId(), memberUniversityDepartment.getUniversityDepartment().getDepartmentGroup().getName());
        return MemberUniversityDepartmentResponse.of(memberUniversityDepartment.getId(), departmentGroupResponse, memberUniversityDepartment.getUniversityDepartment().getUniversity(), memberUniversityDepartment.getUniversityDepartment().getDepartment(), memberUniversityDepartment.getDepartmentType());
    }
}
