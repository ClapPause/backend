package com.clap.pause.dto.memberUniversityDepartment;

import com.clap.pause.dto.departmentGroup.DepartmentGroupResponse;
import com.clap.pause.model.DepartmentType;

public record MemberUniversityDepartmentResponse(
        Long id,
        DepartmentGroupResponse departmentGroupResponse,
        String university,
        String department,
        DepartmentType departmentType
) {
    public static MemberUniversityDepartmentResponse of(Long id, DepartmentGroupResponse departmentGroupResponse, String university, String department, DepartmentType departmentType) {
        return new MemberUniversityDepartmentResponse(id, departmentGroupResponse, university, department, departmentType);
    }
}
