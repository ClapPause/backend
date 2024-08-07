package com.clap.pause.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "department_group")
@SQLDelete(sql = "update department_group set deleted = true where id = ?")
@SQLRestriction("deleted is false")
public class DepartmentGroup extends BaseEntity {
    @NotNull
    @Column(name = "name")
    private String name;
    @NotNull
    @Column(name = "deleted")
    private Boolean deleted = Boolean.FALSE;

    protected DepartmentGroup() {
    }

    public DepartmentGroup(String name) {
        this.name = name;
    }
}
