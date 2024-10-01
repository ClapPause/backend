package com.clap.pause.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Entity
@Table(name = "notification")
@Getter
public class Notification extends BaseEntity {
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;
    @NotNull
    @Column(name = "community")
    private Boolean community = Boolean.TRUE;
    @NotNull
    @Column(name = "recommend_post")
    private Boolean recommendPost = Boolean.TRUE;
    @NotNull
    @Column(name = "new_comment")
    private Boolean newComment = Boolean.TRUE;
    @NotNull
    @Column(name = "my_post_comment")
    private Boolean myPostComment = Boolean.TRUE;
    @NotNull
    @Column(name = "my_post_comment_like")
    private Boolean myPostCommentLike = Boolean.TRUE;
    @NotNull
    @Column(name = "my_post_scrap")
    private Boolean myPostScrap = Boolean.TRUE;
    @NotNull
    @Column(name = "selected_hot_post")
    private Boolean selectedHotPost = Boolean.TRUE;


    protected Notification() {
    }

    public Notification(Member member) {
        this.member = member;
    }
}
