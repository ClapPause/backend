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
@Table(name = "image_vote_option")
@Getter
@SQLDelete(sql = "update image_vote_option set deleted = true where id = ?")
@SQLRestriction("deleted = false")
public class ImageVoteOption extends BaseEntity {
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    private Post post;
    @NotNull
    @Column(name = "image")
    private String image;
    @NotNull
    @Column(name = "description")
    private String description;
    @NotNull
    @Column(name = "deleted")
    @Getter(AccessLevel.PRIVATE)
    private Boolean deleted = Boolean.FALSE;

    protected ImageVoteOption() {
    }

    public ImageVoteOption(Post post, String image, String description) {
        this.post = post;
        this.image = image;
        this.description = description;
    }
}
