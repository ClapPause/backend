package com.clap.pause.service;

import com.clap.pause.exception.NotFoundElementException;
import com.clap.pause.model.UniversityDepartment;
import com.clap.pause.repository.UniversityDepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UniversityDepartmentService {
    private final UniversityDepartmentRepository universityDepartmentRepository;

    public UniversityDepartment getUniversityDepartment(Long universityDepartmentId) {
        return universityDepartmentRepository.findById(universityDepartmentId).orElseThrow(
                () -> new NotFoundElementException("존재하지 않는 학교학과입니다.")
        );
    }
}
