package com.clap.pause.controller;

import com.clap.pause.dto.universityDepartment.UniversityDepartmentRequest;
import com.clap.pause.dto.universityDepartment.UniversityDepartmentResponse;
import com.clap.pause.service.UniversityDepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/universityDepartment")
@RequiredArgsConstructor
public class UniversityDepartmentController {

    private final UniversityDepartmentService universityDepartmentService;

    @PostMapping
    public ResponseEntity<UniversityDepartmentResponse> saveUniversityDepartment(@Valid @RequestBody UniversityDepartmentRequest universityDepartmentRequest) {
        var universityDepartment = universityDepartmentService.saveUniversityDepartment(universityDepartmentRequest);
        return ResponseEntity.created(URI.create("/api/universityDepartment/" + universityDepartment.id())).body(universityDepartment);
    }
}
