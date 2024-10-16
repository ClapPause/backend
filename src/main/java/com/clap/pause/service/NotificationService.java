package com.clap.pause.service;

import com.clap.pause.dto.notification.NotificationResponse;
import com.clap.pause.exception.InvalidRequestException;
import com.clap.pause.exception.NotFoundElementException;
import com.clap.pause.model.Member;
import com.clap.pause.model.Notification;
import com.clap.pause.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public void saveNotification(Member member) {
        if (notificationRepository.existsByMember(member)) {
            throw new InvalidRequestException("이미 이용자의 알림 정보가 존재합니다.");
        }
        var notification = new Notification(member);
        notificationRepository.save(notification);
    }

    public NotificationResponse getNotification(Long memberId) {
        var notification = notificationRepository.findByMemberId(memberId)
                .orElseThrow(() -> new NotFoundElementException("해당 이용자의 알림 정보가 존재하지 않습니다."));
        return getNotificationResponse(notification);
    }

    private NotificationResponse getNotificationResponse(Notification notification) {
        return NotificationResponse.of(
                notification.getBallooningTime(),
                notification.getSimulation(),
                notification.getEvent(),
                notification.getServiceUse(),
                notification.getRecommendPost(),
                notification.getNewComment(),
                notification.getSelectedHotPost(),
                notification.getActivity());
    }
}
