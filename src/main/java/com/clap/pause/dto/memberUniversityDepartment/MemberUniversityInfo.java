package com.clap.pause.dto.memberUniversityDepartment;

public record MemberUniversityInfo(
        Long id,
        String name,
        String university,
        String department
) {
    public static MemberUniversityInfo of(Long id, String name, String university, String department) {
        return new MemberUniversityInfo(id, name, university, department);
    }
}
