package com.advisingscheduler.model;

public class NotificationResponse {

    private boolean success;
    private String message;
    private String notificationId;

    public NotificationResponse() {}

    public NotificationResponse(boolean success, String message, String notificationId) {
        this.success = success;
        this.message = message;
        this.notificationId = notificationId;
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getNotificationId() { return notificationId; }
    public void setNotificationId(String notificationId) { this.notificationId = notificationId; }
}
