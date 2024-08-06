package com.clap.pause.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;

@Entity
@Table(name = "member")
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
    @Column(name = "birth", unique = true)
    private LocalDate birth;
    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;
    @NotNull
    @Column(name = "job")
    private String job;
    @NotNull
    @Column(name = "certified")
    private Boolean certified = Boolean.FALSE;
    @NotNull
    @Column(name = "deleted")
    private Boolean deleted = Boolean.FALSE;

    protected Member() {
    }

    public Member(String email, OauthType oauthType) {
        this.email = email;
        this.password = oauthType.name();
    }

    public Member(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
