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

    public UniversityDepartmentResponse saveUniversityDepartment(UniversityDepartmentRequest universityDepartmentRequest) {
        var universityDepartment = saveUniversityDepartmentWithUniversityDepartmentRequest(universityDepartmentRequest);
        return getUniversityDepartmentResponse(universityDepartment);
    }

    public void updateUniversityDepartment(Long id, UniversityDepartmentRequest universityDepartmentRequest) {
        var universityDepartment = universityDepartmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundElementException(id + "를 가진 대학교의 학과가 존재하지 않습니다."));
        updateUniversityDepartmentWithUniversityDepartmentRequest(universityDepartment, universityDepartmentRequest);
    }

    public UniversityDepartmentResponse getUniversityDepartment(Long id) {
        var universityDepartment = universityDepartmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundElementException(id + "를 가진 대학교의 학과가 존재하지 않습니다."));
        return getUniversityDepartmentResponse(universityDepartment);
    }

    public List<UniversityDepartmentResponse> getUniversityDepartments(String university) {
        var UniversityDepartments = universityDepartmentRepository.findAllByUniversity(university);
        return UniversityDepartments.stream()
                .map(this::getUniversityDepartmentResponse)
                .toList();
    }

    private UniversityDepartment saveUniversityDepartmentWithUniversityDepartmentRequest(UniversityDepartmentRequest universityDepartmentRequest) {
        var departmentGroup = departmentGroupRepository.findById(universityDepartmentRequest.departmentGroupId())
                .orElseThrow(() -> new NotFoundElementException(universityDepartmentRequest.departmentGroupId() + "를 가진 대학교의 학과가 존재하지 않습니다."));
        var universityDepartment = new UniversityDepartment(departmentGroup, universityDepartmentRequest.university(), universityDepartmentRequest.department());
        return universityDepartmentRepository.save(universityDepartment);
    }

    private void updateUniversityDepartmentWithUniversityDepartmentRequest(UniversityDepartment universityDepartment, UniversityDepartmentRequest universityDepartmentRequest) {
        universityDepartment.updateUniversityDepartment(universityDepartmentRequest.university(), universityDepartmentRequest.department());
        universityDepartmentRepository.save(universityDepartment);
    }

    private UniversityDepartmentResponse getUniversityDepartmentResponse(UniversityDepartment universityDepartment) {
        return UniversityDepartmentResponse.of(universityDepartment.getId(), universityDepartment.getDepartmentGroup().getName(), universityDepartment.getUniversity(), universityDepartment.getDepartment());
    }
}
