package com.clap.pause.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;

@Entity
@Table(name = "member")
@Getter
@SQLDelete(sql = "update member set deleted = true where id = ?")
@SQLRestriction("deleted = false")
public class Member extends BaseEntity {
    @NotNull
    @Column(name = "name")
    private String name;
    @NotNull
    @Column(name = "phone_number", unique = true)
    private String phoneNumber;
    @NotNull
    @Column(name = "password")
    private String password;
    @NotNull
    @Column(name = "birth")
    private LocalDate birth;
    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;
    @NotNull
    @Column(name = "job")
    private String job;
    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "member_role")
    private MemberRole memberRole;
    @NotNull
    @Column(name = "certified")
    private Boolean certified = Boolean.FALSE;
    @NotNull
    @Column(name = "deleted")
    @Getter(AccessLevel.PRIVATE)
    private Boolean deleted = Boolean.FALSE;

    protected Member() {
    }

    public Member(String name, String phoneNumber, String password, LocalDate birth, Gender gender, String job) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.birth = birth;
        this.gender = gender;
        this.job = job;
        this.memberRole = MemberRole.MEMBER;
    }
}
