package com.clap.pause.service;

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

}
