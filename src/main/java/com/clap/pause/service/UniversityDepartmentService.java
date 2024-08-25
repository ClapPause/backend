package com.clap.pause.service;

import com.clap.pause.dto.universityDepartment.UniversityDepartmentRequest;
import com.clap.pause.dto.universityDepartment.UniversityDepartmentResponse;
import com.clap.pause.exception.NotFoundElementException;
import com.clap.pause.model.UniversityDepartment;
import com.clap.pause.repository.DepartmentGroupRepository;
import com.clap.pause.repository.UniversityDepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UniversityDepartmentService {

    private final UniversityDepartmentRepository universityDepartmentRepository;
    private final DepartmentGroupRepository departmentGroupRepository;

    /**
     * 학과정보를 저장하는 메서드
     *
     * @param universityDepartmentRequest
     * @return universityDepartment
     */
    public UniversityDepartmentResponse saveUniversityDepartment(UniversityDepartmentRequest universityDepartmentRequest) {
        var universityDepartment = saveUniversityDepartmentWithUniversityDepartmentRequest(universityDepartmentRequest);
        return getUniversityDepartmentResponse(universityDepartment);
    }

    /**
     * 학과정보를 수정하는 메서드
     *
     * @param id
     * @param universityDepartmentRequest
     */
    public void updateUniversityDepartment(Long id, UniversityDepartmentRequest universityDepartmentRequest) {
        var universityDepartment = universityDepartmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundElementException(id + "를 가진 대학교의 학과가 존재하지 않습니다."));
        updateUniversityDepartmentWithUniversityDepartmentRequest(universityDepartment, universityDepartmentRequest);
    }

    /**
     * id 를 사용하여 학과정보를 조회하는 메서드
     *
     * @param id
     * @return universityDepartmentResponse
     */
    public UniversityDepartmentResponse getUniversityDepartment(Long id) {
        var universityDepartment = universityDepartmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundElementException(id + "를 가진 대학교의 학과가 존재하지 않습니다."));
        return getUniversityDepartmentResponse(universityDepartment);
    }

    /**
     * 학교 이름을 바탕으로 해당 학교의 저장된 학과를 조회하는 메서드
     *
     * @param university
     * @return universityDepartments
     */
    public List<UniversityDepartmentResponse> getUniversityDepartments(String university) {
        var universityDepartments = universityDepartmentRepository.findAllByUniversity(university);
        return universityDepartments.stream()
                .map(this::getUniversityDepartmentResponse)
                .toList();
    }

    /**
     * RequestDTO 를 사용하여 학과 정보를 저장하는 메서드
     *
     * @param universityDepartmentRequest
     * @return universityDepartment
     */
    private UniversityDepartment saveUniversityDepartmentWithUniversityDepartmentRequest(UniversityDepartmentRequest universityDepartmentRequest) {
        var departmentGroup = departmentGroupRepository.findById(universityDepartmentRequest.departmentGroupId())
                .orElseThrow(() -> new NotFoundElementException(universityDepartmentRequest.departmentGroupId() + "를 가진 대학교의 학과가 존재하지 않습니다."));
        var universityDepartment = new UniversityDepartment(departmentGroup, universityDepartmentRequest.university(), universityDepartmentRequest.department());
        return universityDepartmentRepository.save(universityDepartment);
    }

    /**
     * 학과정보와 RequestDTO 를 사용해서 학과정보를 수정하는 메서드
     *
     * @param universityDepartment
     * @param universityDepartmentRequest
     */
    private void updateUniversityDepartmentWithUniversityDepartmentRequest(UniversityDepartment universityDepartment, UniversityDepartmentRequest universityDepartmentRequest) {
        universityDepartment.updateUniversityDepartment(universityDepartmentRequest.university(), universityDepartmentRequest.department());
        universityDepartmentRepository.save(universityDepartment);
    }

    /**
     * 학과정보 엔티티를 ResponseDTO 로 변환하는 메서드
     *
     * @param universityDepartment
     * @return universityDepartmentResponse
     */
    private UniversityDepartmentResponse getUniversityDepartmentResponse(UniversityDepartment universityDepartment) {
        return UniversityDepartmentResponse.of(universityDepartment.getId(), universityDepartment.getDepartmentGroup().getName(), universityDepartment.getUniversity(), universityDepartment.getDepartment());
    }
}
