package com.clap.pause.controller;

import com.clap.pause.dto.notification.NotificationResponse;
import com.clap.pause.model.NotificationField;
import com.clap.pause.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<NotificationResponse> getNotification() {
        var notification = notificationService.getNotification(getMemberId());
        return ResponseEntity.ok(notification);
    }

    @PutMapping("/all")
    public ResponseEntity<Void> updateAll(@RequestParam Boolean value) {
        notificationService.updateAll(getMemberId(), value);
        return ResponseEntity.ok()
                .build();
    }

    @PutMapping("/community")
    public ResponseEntity<Void> updateCommunity(@RequestParam Boolean value) {
        notificationService.updateCommunity(getMemberId(), value);
        return ResponseEntity.ok()
                .build();
    }

    @PatchMapping
    public ResponseEntity<Void> updateField(@RequestParam NotificationField field, @RequestParam Boolean value) {
        notificationService.updateField(getMemberId(), field, value);
        return ResponseEntity.ok()
                .build();
    }

    private Long getMemberId() {
        var authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        return (Long) authentication.getPrincipal();
    }
}
