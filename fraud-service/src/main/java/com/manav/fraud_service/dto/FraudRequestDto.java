package com.manav.fraud_service.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FraudRequestDto {
    private String ipAddress;
    private String userAgent;
    private String path;
}
