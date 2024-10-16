package com.clap.pause.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Entity
@Table(name = "notification")
@Getter
public class Notification extends BaseEntity {
    @NotNull
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;
    @NotNull
    @Column(name = "ballooning_time")
    private Boolean ballooningTime = Boolean.TRUE;
    @NotNull
    @Column(name = "simulation")
    private Boolean simulation = Boolean.TRUE;
    @NotNull
    @Column(name = "event")
    private Boolean event = Boolean.TRUE;
    @NotNull
    @Column(name = "service_use")
    private Boolean serviceUse = Boolean.TRUE;
    @NotNull
    @Column(name = "recommend_post")
    private Boolean recommendPost = Boolean.TRUE;
    @NotNull
    @Column(name = "new_comment")
    private Boolean newComment = Boolean.TRUE;
    @NotNull
    @Column(name = "selected_hot_post")
    private Boolean selectedHotPost = Boolean.TRUE;
    @NotNull
    @Column(name = "activity")
    private Boolean activity = Boolean.TRUE;


    protected Notification() {
    }

    public Notification(Member member) {
        this.member = member;
    }

    public void updateAll(Boolean value) {
        this.ballooningTime = value;
        this.simulation = value;
        this.event = value;
        this.serviceUse = value;
        this.recommendPost = value;
        this.newComment = value;
        this.selectedHotPost = value;
        this.activity = value;
    }

    public void updateCommunity(Boolean value) {
        this.recommendPost = value;
        this.newComment = value;
        this.selectedHotPost = value;
        this.activity = value;
    }

    public void updateBallooningTime(Boolean ballooningTime) {
        this.ballooningTime = ballooningTime;
    }

    public void updateSimulation(Boolean simulation) {
        this.simulation = simulation;
    }

    public void updateEvent(Boolean event) {
        this.event = event;
    }

    public void updateServiceUse(Boolean serviceUse) {
        this.serviceUse = serviceUse;
    }

    public void updateRecommendPost(Boolean recommendPost) {
        this.recommendPost = recommendPost;
    }

    public void updateNewComment(Boolean newComment) {
        this.newComment = newComment;
    }

    public void updateActivity(Boolean activity) {
        this.activity = activity;
    }

    public void updateSelectedHotPost(Boolean selectedHotPost) {
        this.selectedHotPost = selectedHotPost;
    }
}
