package com.advisingscheduler.model;

public class Advisor {

    private int advisorId;
    private String fullName;
    private String specialization;

    public Advisor() {}

    public Advisor(int advisorId, String fullName, String specialization) {
        this.advisorId = advisorId;
        this.fullName = fullName;
        this.specialization = specialization;
    }

    public int getAdvisorId() { return advisorId; }
    public void setAdvisorId(int advisorId) { this.advisorId = advisorId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
}
