package com.manav.fraud_service.service;

import com.manav.fraud_service.dto.FraudRequestDto;
import com.manav.fraud_service.dto.FraudResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FraudAnalysisService {

    private double calculateRiskScore(FraudRequestDto request) {
        double score = 0.1;

        if(request.getUserAgent() == null || request.getUserAgent().isEmpty()){
            score += 0.4;
        }

        if(request.getIpAddress().startsWith("192")) {
            score += 0.2;
        }

        if(request.getPath().contains("admin")) {
            score += 0.5;
        }

        return Math.min(score,1.0);

    }

    public FraudResponseDto analyze(FraudRequestDto request){

        double riskScore = calculateRiskScore(request);

        if(riskScore > 0.8){
            return new FraudResponseDto("BLOCK", riskScore,"High risk detected");
        }
        if( riskScore > 0.5){
            return new FraudResponseDto("FLAG", riskScore,"suspicious activity");
        }
        return new FraudResponseDto("ALLOW", riskScore,"low risk detected");
    }
}
