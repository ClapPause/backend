package com.clap.pause.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "member_university_department")
@SQLDelete(sql = "update member_university_department set deleted = true where id = ?")
@SQLRestriction("deleted is false")
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
    @Column(name = "deleted")
    private Boolean deleted = Boolean.FALSE;

    protected MemberUniversityDepartment() {
    }

    public MemberUniversityDepartment(Member member, UniversityDepartment universityDepartment) {
        this.member = member;
        this.universityDepartment = universityDepartment;
    }
}
