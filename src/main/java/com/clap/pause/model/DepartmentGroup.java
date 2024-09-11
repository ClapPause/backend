package com.clap.pause.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "department_group")
@Getter
@SQLDelete(sql = "update department_group set deleted = true where id = ?")
@SQLRestriction("deleted = false")
public class DepartmentGroup extends BaseEntity {
    @NotNull
    @Column(name = "name", unique = true)
    private String name;
    @NotNull
    @Column(name = "deleted")
    @Getter(AccessLevel.PRIVATE)
    private Boolean deleted = Boolean.FALSE;

    protected DepartmentGroup() {
    }

    public DepartmentGroup(String name) {
        this.name = name;
    }

    public void updateName(String name) {
        this.name = name;
    }
}
