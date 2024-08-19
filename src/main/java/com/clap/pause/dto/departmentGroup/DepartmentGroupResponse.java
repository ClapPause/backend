package com.clap.pause.dto.departmentGroup;

public record DepartmentGroupResponse(
        String name
) {
    public static DepartmentGroupResponse of(String name) {
        return new DepartmentGroupResponse(name);
    }
}
