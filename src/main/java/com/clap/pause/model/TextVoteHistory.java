package com.clap.pause.model;

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
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "text_vote_history")
@Getter
@SQLDelete(sql = "update text_vote_history set deleted = true where id = ?")
@SQLRestriction("deleted = false")
public class TextVoteHistory extends BaseEntity {
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "text_vote_option_id", referencedColumnName = "id")
    private TextVoteOption textVoteOption;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;
    @NotNull
    @Column(name = "deleted")
    @Getter(AccessLevel.PRIVATE)
    private Boolean deleted = Boolean.FALSE;

    protected TextVoteHistory() {
    }

    public TextVoteHistory(TextVoteOption textVoteOption, Member member) {
        this.textVoteOption = textVoteOption;
        this.member = member;
    }
}
