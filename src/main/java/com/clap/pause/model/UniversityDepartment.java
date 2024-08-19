package com.clap.pause.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "university_department")
@Getter
@SQLDelete(sql = "update university_department set deleted = true where id = ?")
@SQLRestriction("deleted is false")
public class UniversityDepartment extends BaseEntity {
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_group_id", referencedColumnName = "id")
    private DepartmentGroup departmentGroup;
    @NotNull
    @Column(name = "university")
    private String university;
    @NotNull
    @Column(name = "university_department")
    private String universityDepartment;
    @NotNull
    @Column(name = "deleted")
    @Getter(AccessLevel.PRIVATE)
    private Boolean deleted = Boolean.FALSE;

    protected UniversityDepartment() {
    }

    public UniversityDepartment(DepartmentGroup departmentGroup, String university, String universityDepartment) {
        this.departmentGroup = departmentGroup;
        this.university = university;
        this.universityDepartment = universityDepartment;
    }
}
