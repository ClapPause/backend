package com.clap.pause.controller;

import com.clap.pause.dto.memberUniversityDepartment.CertificateRequest;
import com.clap.pause.dto.memberUniversityDepartment.CertificateResponse;
import com.clap.pause.service.MemberService;
import com.clap.pause.service.MemberUniversityDepartmentService;
import com.clap.pause.service.UniversityDepartmentService;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members/certificate")
@RequiredArgsConstructor
public class MemberUniversityDepartmentController {

    private final MemberUniversityDepartmentService memberUniversityDepartmentService;
    private final MemberService memberService;
    private final UniversityDepartmentService universityDepartmentService;

    /**
     * 멤버가 학교학과를 인증하는 메소드
     *
     * @param certificateRequest
     * @return
     */
    @PostMapping
    public ResponseEntity<CertificateResponse> certificateMember(@Valid @RequestBody
                                                                 CertificateRequest certificateRequest) {
        var member = memberService.getMember(certificateRequest.memberId());
        var universityDepartment =
                universityDepartmentService.getUniversityDepartment(certificateRequest.universityDepartmentId());

        var certificateResponse =
                memberUniversityDepartmentService.certificateMember(member, universityDepartment,
                        certificateRequest.departmentType());
        return ResponseEntity.created(
                        URI.create("/api/members/certificate" + certificateResponse.memberUniversityDepartmentId()))
                .body(certificateResponse);
    }

}
