package com.clap.pause.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "member_university_department")
@Getter
@SQLDelete(sql = "update member_university_department set deleted = true where id = ?")
@SQLRestriction("deleted = false")
public class MemberUniversityDepartment extends BaseEntity {
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "university_department_id", referencedColumnName = "id")
    private UniversityDepartment universityDepartment;
    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "department_type")
    private DepartmentType departmentType;
    @NotNull
    @Column(name = "deleted")
    @Getter(AccessLevel.PRIVATE)
    private Boolean deleted = Boolean.FALSE;

    protected MemberUniversityDepartment() {
    }

    public MemberUniversityDepartment(Member member, UniversityDepartment universityDepartment, DepartmentType departmentType) {
        this.member = member;
        this.universityDepartment = universityDepartment;
        this.departmentType = departmentType;
    }
}
