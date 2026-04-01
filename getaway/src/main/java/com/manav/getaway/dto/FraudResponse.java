package com.manav.getaway.dto;

public class FraudResponse {

    private String status;
    private double riskScore;
    private String reason;

    public String getStatus() {
        return status;
    }

    public double getRiskScore() {
        return riskScore;
    }

    public String getReason() {
        return reason;
    }
}
