package com.advisingscheduler.model;

import java.time.LocalDateTime;

public class Appointment {

    private int appointmentId;
    private int clientId;
    private int slotId;
    private int serviceId;
    private String status;
    private LocalDateTime bookedAt;
    private String notes;

    // Joined fields for display purposes
    private String clientName;
    private String serviceName;
    private LocalDateTime slotStartTime;
    private LocalDateTime slotEndTime;

    public Appointment() {}

    public int getAppointmentId() { return appointmentId; }
    public void setAppointmentId(int appointmentId) { this.appointmentId = appointmentId; }

    public int getClientId() { return clientId; }
    public void setClientId(int clientId) { this.clientId = clientId; }

    public int getSlotId() { return slotId; }
    public void setSlotId(int slotId) { this.slotId = slotId; }

    public int getServiceId() { return serviceId; }
    public void setServiceId(int serviceId) { this.serviceId = serviceId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getBookedAt() { return bookedAt; }
    public void setBookedAt(LocalDateTime bookedAt) { this.bookedAt = bookedAt; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getClientName() { return clientName; }
    public void setClientName(String clientName) { this.clientName = clientName; }

    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }

    public LocalDateTime getSlotStartTime() { return slotStartTime; }
    public void setSlotStartTime(LocalDateTime slotStartTime) { this.slotStartTime = slotStartTime; }

    public LocalDateTime getSlotEndTime() { return slotEndTime; }
    public void setSlotEndTime(LocalDateTime slotEndTime) { this.slotEndTime = slotEndTime; }
}
