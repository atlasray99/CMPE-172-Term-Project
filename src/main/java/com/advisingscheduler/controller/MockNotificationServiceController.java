package com.advisingscheduler.controller;

import com.advisingscheduler.model.NotificationRequest;
import com.advisingscheduler.model.NotificationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/external/notifications")
public class MockNotificationServiceController {

    private static final Logger logger = LoggerFactory.getLogger(MockNotificationServiceController.class);

    @PostMapping("/send")
    public NotificationResponse sendNotification(@RequestBody NotificationRequest request) {
        logger.info("[EXT-NOTIFICATION] Received notification request for appointment {}",
                request.getAppointmentId());
        logger.info("[EXT-NOTIFICATION] Sending confirmation to {} <{}>",
                request.getRecipientName(), request.getRecipientEmail());
        logger.info("[EXT-NOTIFICATION] Service: {} on {} at {}",
                request.getServiceName(), request.getAppointmentDate(),
                request.getAppointmentTime());

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        String notificationId = "NOTIF-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        logger.info("[EXT-NOTIFICATION] Notification {} delivered successfully", notificationId);

        return new NotificationResponse(
                true,
                "Confirmation email sent to " + request.getRecipientEmail(),
                notificationId
        );
    }
}
