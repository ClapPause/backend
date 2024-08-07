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
@Table(name = "text_vote_history")
@SQLDelete(sql = "update text_vote_history set deleted = true where id = ?")
@SQLRestriction("deleted is false")
public class TextVoteHistory extends BaseEntity {
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "text_vote_item_id", referencedColumnName = "id")
    private TextVoteItem textVoteItem;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;
    @NotNull
    @Column(name = "deleted")
    private Boolean deleted = Boolean.FALSE;

    protected TextVoteHistory() {
    }

    public TextVoteHistory(TextVoteItem textVoteItem, Member member) {
        this.textVoteItem = textVoteItem;
        this.member = member;
    }
}
