package com.manav.fraud_service.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FraudResponseDto {

    private String status;
    private double riskScore;
    private String reason;

}
