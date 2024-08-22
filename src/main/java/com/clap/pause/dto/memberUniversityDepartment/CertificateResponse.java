package com.clap.pause.dto.memberUniversityDepartment;

import com.clap.pause.model.DepartmentType;

public record CertificateResponse(
        Long memberUniversityDepartmentId,
        Long memberId,
        Long universityDepartmentId,
        DepartmentType departmentType
) {
}
