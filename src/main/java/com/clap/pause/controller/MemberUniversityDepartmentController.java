package com.clap.pause.controller;

import com.clap.pause.service.MemberUniversityDepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members/certificate")
@RequiredArgsConstructor
public class MemberUniversityDepartmentController {

    private final MemberUniversityDepartmentService memberUniversityDepartmentService;


}
