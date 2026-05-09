package com.advisingscheduler.service;

import com.advisingscheduler.model.NotificationRequest;
import com.advisingscheduler.model.NotificationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class NotificationClient {

    private static final Logger logger = LoggerFactory.getLogger(NotificationClient.class);

    private final RestTemplate restTemplate;
    private final String notificationServiceUrl;

    public NotificationClient(
            RestTemplate restTemplate,
            @Value("${notification.service.url:http://localhost:8080/api/external/notifications}")
            String notificationServiceUrl) {
        this.restTemplate = restTemplate;
        this.notificationServiceUrl = notificationServiceUrl;
    }

    public String sendBookingConfirmation(int appointmentId, String recipientEmail,
                                          String recipientName, String serviceName,
                                          String appointmentDate, String appointmentTime) {
        NotificationRequest request = new NotificationRequest(
                recipientEmail,
                recipientName,
                "Booking Confirmation — Appointment #" + appointmentId,
                serviceName,
                appointmentDate,
                appointmentTime,
                appointmentId
        );

        String url = notificationServiceUrl + "/send";
        logger.info("Calling external notification service at {}", url);

        try {
            NotificationResponse response = restTemplate.postForObject(
                    url, request, NotificationResponse.class);

            if (response != null && response.isSuccess()) {
                logger.info("Notification sent successfully: {}", response.getNotificationId());
                return response.getNotificationId();
            } else {
                logger.warn("Notification service returned failure response");
                return null;
            }
        } catch (RestClientException e) {
            logger.error("Failed to reach notification service: {}", e.getMessage());
            return null;
        }
    }
}
