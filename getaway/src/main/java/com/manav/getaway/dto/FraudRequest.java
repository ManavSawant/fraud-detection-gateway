package com.manav.getaway.dto;

public class FraudRequest {

    private String ipAddress;
    private String userAgent;
    private String path;

    public FraudRequest(String ipAddress, String userAgent, String path) {
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.path = path;
    }
}
