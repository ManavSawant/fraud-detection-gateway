package com.manav.fraud_service.dto;


import lombok.Getter;

@Getter
public class FraudRequestDto {
    private String ipAddress;
    private String userAgent;
    private String path;
}
