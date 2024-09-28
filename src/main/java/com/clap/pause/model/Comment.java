package com.clap.pause.model;

import com.clap.pause.exception.InvalidRequestException;
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

@Entity
@Table(name = "comments")
@Getter
@SQLDelete(sql = "update comments set deleted = true where id = ?")
public class Comment extends BaseEntity {
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    private Post post;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id", referencedColumnName = "id")
    private Comment parentComment;
    @NotNull
    @Column(name = "contents")
    private String contents;
    @NotNull
    @Column(name = "deleted")
    @Getter(AccessLevel.PRIVATE)
    private Boolean deleted = Boolean.FALSE;

    protected Comment() {
    }

    public Comment(Member member, Post post, String contents) {
        this.member = member;
        this.post = post;
        this.contents = contents;
    }

    public Comment(Member member, Post post, Comment parentComment, String contents) {
        this.member = member;
        this.post = post;
        this.parentComment = parentComment;
        this.contents = contents;
    }

    public void updateContents(String newContents) {
        this.contents = newContents;
    }

    public void validatePost(Long postId) {
        if (!this.post.getId().equals(postId)) {
            throw new InvalidRequestException(postId + "를 가진 게시글에 존재하지 않는 댓글입니다.");
        }
    }
}
