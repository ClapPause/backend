package com.clap.pause.service;

import com.clap.pause.repository.MemberUniversityDepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberUniversityDepartmentService {
    private final MemberUniversityDepartmentRepository memberUniversityDepartmentRepository;
}
