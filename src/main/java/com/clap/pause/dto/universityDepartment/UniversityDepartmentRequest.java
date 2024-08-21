package com.clap.pause.dto.universityDepartment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UniversityDepartmentRequest(
        @NotNull(message = "학과 그룹은 반드시 선택되어야 합니다.")
        Long departmentGroupId,
        @NotBlank(message = "대학명은 최소 1자 이상이어야 합니다.")
        String university,
        @NotBlank(message = "대학교 학과명은 최소 1자 이상이어야 합니다.")
        String universityDepartment
) {
}
