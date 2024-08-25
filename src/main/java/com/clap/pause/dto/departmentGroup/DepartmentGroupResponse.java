package com.clap.pause.dto.departmentGroup;

public record DepartmentGroupResponse(
        Long id,
        String name
) {
    public static DepartmentGroupResponse of(Long id, String name) {
        return new DepartmentGroupResponse(id, name);
    }
}
