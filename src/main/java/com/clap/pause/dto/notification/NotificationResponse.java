package com.clap.pause.dto.notification;

public record NotificationResponse(
        Boolean ballooningTime, Boolean simulation, Boolean event, Boolean serviceUse, Boolean recommendPost, Boolean newComment, Boolean selectedHotPost, Boolean activity
) {
    public static NotificationResponse of(
            Boolean ballooningTime, Boolean simulation, Boolean event, Boolean serviceUse, Boolean recommendPost, Boolean newComment, Boolean selectedHotPost, Boolean activity
    ) {
        return new NotificationResponse(ballooningTime, simulation, event, serviceUse, recommendPost, newComment, selectedHotPost, activity);
    }
}
