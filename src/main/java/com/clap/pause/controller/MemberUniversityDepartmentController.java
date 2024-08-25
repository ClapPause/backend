package com.clap.pause.controller;

import com.clap.pause.dto.memberUniversityDepartment.MemberUniversityDepartmentRequest;
import com.clap.pause.dto.memberUniversityDepartment.MemberUniversityDepartmentResponse;
import com.clap.pause.service.MemberUniversityDepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/memberUniversityDepartment")
@RequiredArgsConstructor
public class MemberUniversityDepartmentController {

    private final MemberUniversityDepartmentService memberUniversityDepartmentService;

    @PostMapping
    public ResponseEntity<MemberUniversityDepartmentResponse> saveMemberUniversityDepartment(@Valid @RequestBody MemberUniversityDepartmentRequest memberUniversityDepartmentRequest) {
        var memberId = getMemberId();
        var memberUniversityDepartment = memberUniversityDepartmentService.saveMemberUniversityDepartment(memberId, memberUniversityDepartmentRequest);
        return ResponseEntity.created(URI.create("/api/memberUniversityDepartment/" + memberUniversityDepartment.id()))
                .body(memberUniversityDepartment);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberUniversityDepartmentResponse> getMemberUniversityDepartment(@PathVariable Long id) {
        var memberUniversityDepartment = memberUniversityDepartmentService.getMemberUniversityDepartment(id);
        return ResponseEntity.ok(memberUniversityDepartment);
    }

    @GetMapping
    public ResponseEntity<List<MemberUniversityDepartmentResponse>> getMemberUniversityDepartments() {
        var memberId = getMemberId();
        var memberUniversityDepartments = memberUniversityDepartmentService.getMemberUniversityDepartments(memberId);
        return ResponseEntity.ok(memberUniversityDepartments);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMemberUniversityDepartment(@PathVariable Long id) {
        memberUniversityDepartmentService.deleteMemberUniversityDepartment(id);
        return ResponseEntity.noContent()
                .build();
    }

    private Long getMemberId() {
        var authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        return (Long) authentication.getPrincipal();
    }
}
