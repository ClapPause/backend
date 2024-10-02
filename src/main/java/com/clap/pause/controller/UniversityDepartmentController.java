package com.clap.pause.controller;

import com.clap.pause.dto.universityDepartment.UniversityDepartmentRequest;
import com.clap.pause.dto.universityDepartment.UniversityDepartmentResponse;
import com.clap.pause.service.UniversityDepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/university-departments")
@RequiredArgsConstructor
public class UniversityDepartmentController {

    private final UniversityDepartmentService universityDepartmentService;

    @PostMapping
    public ResponseEntity<Void> saveUniversityDepartment(@Valid @RequestBody UniversityDepartmentRequest universityDepartmentRequest) {
        universityDepartmentService.saveUniversityDepartment(universityDepartmentRequest);
        return ResponseEntity.ok()
                .build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUniversityDepartment(@PathVariable Long id,
                                                           @Valid @RequestBody UniversityDepartmentRequest universityDepartmentRequest) {
        universityDepartmentService.updateUniversityDepartment(id, universityDepartmentRequest);
        return ResponseEntity.noContent()
                .build();
    }

    @GetMapping
    public ResponseEntity<List<UniversityDepartmentResponse>> getUniversityDepartments(@RequestParam String university,
                                                                                       @RequestParam String department) {
        var universityDepartments = universityDepartmentService.getUniversityDepartmentResponses(university, department);
        return ResponseEntity.ok(universityDepartments);
    }
}
