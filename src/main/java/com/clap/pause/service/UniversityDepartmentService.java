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

    private UniversityDepartment saveUniversityDepartmentWithUniversityDepartmentRequest(UniversityDepartmentRequest universityDepartmentRequest) {
        var departmentGroup = departmentGroupRepository.findById(universityDepartmentRequest.departmentGroupId())
                .orElseThrow(() -> new NotFoundElementException(universityDepartmentRequest.departmentGroupId() + "를 가진 학과그룹이 존재하지 않습니다."));
        var universityDepartment = new UniversityDepartment(departmentGroup, universityDepartmentRequest.university(), universityDepartmentRequest.universityDepartment());
        return universityDepartmentRepository.save(universityDepartment);
    }

    private UniversityDepartmentResponse getUniversityDepartmentResponse(UniversityDepartment universityDepartment) {
        return UniversityDepartmentResponse.of(universityDepartment.getId(), universityDepartment.getDepartmentGroup().getName(), universityDepartment.getUniversity(), universityDepartment.getUniversityDepartment());
    }

}
