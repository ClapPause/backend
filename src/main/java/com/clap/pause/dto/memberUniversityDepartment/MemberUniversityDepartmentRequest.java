package com.clap.pause.dto.memberUniversityDepartment;

import com.clap.pause.model.DepartmentType;
import jakarta.validation.constraints.NotNull;

public record MemberUniversityDepartmentRequest(
        @NotNull(message = "대학교의 학과는 반드시 선택되어야 합니다.")
        Long universityDepartmentId,
        @NotNull(message = "학위 타입은 반드시 선택되어야 합니다.")
        DepartmentType departmentType
) {
}
