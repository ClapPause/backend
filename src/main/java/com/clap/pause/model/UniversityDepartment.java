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
@Table(name = "university_department")
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
    @Column(name = "department")
    private String department;
    @NotNull
    @Column(name = "deleted")
    private Boolean deleted = Boolean.FALSE;

    protected UniversityDepartment() {
    }

    public UniversityDepartment(String university, String department) {
        this.university = university;
        this.department = department;
    }
}
