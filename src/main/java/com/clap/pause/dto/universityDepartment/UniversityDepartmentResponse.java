package com.clap.pause.dto.universityDepartment;

public record UniversityDepartmentResponse(
        Long id,
        String departmentGroup,
        String university,
        String universityDepartment
) {
    public static UniversityDepartmentResponse of(Long id, String departmentGroup, String university, String universityDepartment) {
        return new UniversityDepartmentResponse(id, departmentGroup, university, universityDepartment);
    }
}
