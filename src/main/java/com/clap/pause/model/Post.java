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
@Table(name = "post")
@Getter
@SQLDelete(sql = "update post set deleted = true where id = ?")
@SQLRestriction("deleted = false")
public class Post extends BaseEntity {
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_group_id", referencedColumnName = "id")
    private DepartmentGroup departmentGroup;
    @NotNull
    @Column(name = "title")
    private String title;
    @NotNull
    @Column(name = "contents")
    private String contents;
    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "post_category")
    private PostCategory postCategory;
    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "post_type")
    private PostType postType;
    @NotNull
    @Column(name = "deleted")
    @Getter(AccessLevel.PRIVATE)
    private Boolean deleted = Boolean.FALSE;

    protected Post() {
    }

    public Post(Member member, DepartmentGroup departmentGroup, String title, String contents,
            PostCategory postCategory, PostType postType) {
        this.member = member;
        this.departmentGroup = departmentGroup;
        this.title = title;
        this.contents = contents;
        this.postCategory = postCategory;
        this.postType = postType;
    }

    public void updatePost(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }
}
