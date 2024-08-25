package com.clap.pause.dto.universityDepartment;

public record UniversityDepartmentResponse(
        Long id,
        String departmentGroup,
        String university,
        String department
) {
    public static UniversityDepartmentResponse of(Long id, String departmentGroup, String university, String department) {
        return new UniversityDepartmentResponse(id, departmentGroup, university, department);
    }
}
