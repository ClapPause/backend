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
@Table(name = "image")
@Getter
@SQLDelete(sql = "update image set deleted = true where id = ?")
@SQLRestriction("deleted is false")
public class Image extends BaseEntity {
    @NotNull
    @Column(name = "url")
    private String url;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    private Post post;

    @NotNull
    @Column(name = "deleted")
    @Getter(AccessLevel.PRIVATE)
    private Boolean deleted = Boolean.FALSE;

    protected Image() {
    }

    public Image(String url, Post post) {
        this.url = url;
        this.post = post;
    }
}
