package com.clap.pause.dto.departmentGroup;

import jakarta.validation.constraints.NotBlank;

public record DepartmentGroupRequest(
        @NotBlank(message = "학과 그룹의 이름은 1글자 이상이어야 합니다.")
        String name
) {
}
