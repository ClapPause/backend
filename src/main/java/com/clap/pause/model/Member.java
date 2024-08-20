package com.clap.pause.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import java.time.LocalDate;
import lombok.Getter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@Table(name = "member")
@Getter
@SQLDelete(sql = "update member set deleted = true where id = ?")
@SQLRestriction("deleted is false")
public class Member extends BaseEntity {
    @NotNull
    @Column(name = "name")
    private String name;
    @NotNull
    @Column(name = "email", unique = true)
    private String email;
    @NotNull
    @Column(name = "password")
    private String password;
    @NotNull
    @Column(name = "profile_image")
    private String profileImage;
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
    @Column(name = "phone_number")
    private String phoneNumber;
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

    public Member(String name, String email, OauthType oauthType, String profileImage, LocalDate birth, Gender gender, String job, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.password = oauthType.name();
        this.profileImage = profileImage;
        this.birth = birth;
        this.gender = gender;
        this.job = job;
        this.phoneNumber = phoneNumber;
        this.memberRole = MemberRole.MEMBER;
    }

    public Member(String name, String email, String password, String profileImage, LocalDate birth, Gender gender, String job, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.profileImage = profileImage;
        this.birth = birth;
        this.gender = gender;
        this.job = job;
        this.phoneNumber = phoneNumber;
        this.memberRole = MemberRole.MEMBER;
    }
}
