package com.advisingscheduler.model;

import java.time.LocalDateTime;

public class AvailabilitySlot {

    private int slotId;
    private int advisorId;
    private String advisorName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean isBooked;
    private int version;

    public AvailabilitySlot() {}

    public AvailabilitySlot(int slotId, int advisorId, LocalDateTime startTime,
                            LocalDateTime endTime, boolean isBooked, int version) {
        this.slotId = slotId;
        this.advisorId = advisorId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isBooked = isBooked;
        this.version = version;
    }

    public int getSlotId() { return slotId; }
    public void setSlotId(int slotId) { this.slotId = slotId; }

    public int getAdvisorId() { return advisorId; }
    public void setAdvisorId(int advisorId) { this.advisorId = advisorId; }

    public String getAdvisorName() { return advisorName; }
    public void setAdvisorName(String advisorName) { this.advisorName = advisorName; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public boolean isBooked() { return isBooked; }
    public void setBooked(boolean booked) { isBooked = booked; }

    public int getVersion() { return version; }
    public void setVersion(int version) { this.version = version; }
}
