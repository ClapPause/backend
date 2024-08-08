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
@Table(name = "image_vote_history")
@SQLDelete(sql = "update image_vote_history set deleted = true where id = ?")
@SQLRestriction("deleted is false")
public class ImageVoteHistory extends BaseEntity {
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_vote_option_id", referencedColumnName = "id")
    private ImageVoteOption imageVoteOption;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;
    @NotNull
    @Column(name = "deleted")
    private Boolean deleted = Boolean.FALSE;

    protected ImageVoteHistory() {
    }

    public ImageVoteHistory(ImageVoteOption imageVoteOption, Member member) {
        this.imageVoteOption = imageVoteOption;
        this.member = member;
    }
}
