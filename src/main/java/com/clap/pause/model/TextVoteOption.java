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
@Table(name = "text_vote_option")
@SQLDelete(sql = "update text_vote_option set deleted = true where id = ?")
@SQLRestriction("deleted is false")
public class TextVoteOption extends BaseEntity {
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    private Post post;
    @NotNull
    @Column(name = "text")
    private String text;
    @NotNull
    @Column(name = "deleted")
    private Boolean deleted = Boolean.FALSE;

    protected TextVoteOption() {
    }

    public TextVoteOption(Post post, String text) {
        this.post = post;
        this.text = text;
    }
}
