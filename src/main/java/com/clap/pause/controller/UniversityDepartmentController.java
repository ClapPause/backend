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

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/university-departments")
@RequiredArgsConstructor
public class UniversityDepartmentController {

    private final UniversityDepartmentService universityDepartmentService;

    @PostMapping
    public ResponseEntity<Void> saveUniversityDepartment(@Valid @RequestBody UniversityDepartmentRequest universityDepartmentRequest) {
        var universityDepartment = universityDepartmentService.saveUniversityDepartment(universityDepartmentRequest);
        return ResponseEntity.created(URI.create("/api/university-departments/" + universityDepartment.id()))
                .build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUniversityDepartment(@PathVariable Long id, @Valid @RequestBody UniversityDepartmentRequest universityDepartmentRequest) {
        universityDepartmentService.updateUniversityDepartment(id, universityDepartmentRequest);
        return ResponseEntity.noContent()
                .build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UniversityDepartmentResponse> getUniversityDepartment(@PathVariable Long id) {
        var universityDepartment = universityDepartmentService.getUniversityDepartment(id);
        return ResponseEntity.ok(universityDepartment);
    }

    @GetMapping
    public ResponseEntity<List<UniversityDepartmentResponse>> getUniversityDepartments(@RequestParam String university) {
        var universityDepartments = universityDepartmentService.getUniversityDepartments(university);
        return ResponseEntity.ok(universityDepartments);
    }
}
