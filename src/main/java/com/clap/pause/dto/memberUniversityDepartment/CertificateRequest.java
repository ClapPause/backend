package com.clap.pause.dto.memberUniversityDepartment;

import com.clap.pause.model.DepartmentType;
import jakarta.validation.constraints.NotNull;

public record CertificateRequest(
        @NotNull(message = "학교학과 정보가 필요합니다")
        Long universityDepartmentId,
        @NotNull(message = "멤버 정보가 필요합니다")
        Long memberId,
        @NotNull(message = "학과 타입이 필요합니다")
        DepartmentType departmentType
) {
}
