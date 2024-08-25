package com.clap.pause.dto.memberUniversityDepartment;

import com.clap.pause.model.DepartmentType;

public record MemberUniversityDepartmentResponse(
        Long id,
        String university,
        String department,
        DepartmentType departmentType
) {
    public static MemberUniversityDepartmentResponse of(Long id, String university, String department, DepartmentType departmentType) {
        return new MemberUniversityDepartmentResponse(id, university, department, departmentType);
    }
}
