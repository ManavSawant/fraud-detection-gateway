package com.manav.fraud_service.service;

import com.manav.fraud_service.dto.FraudRequestDto;
import com.manav.fraud_service.dto.FraudResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FraudAnalysisService {

    private final AiFraudService aiFraudService;

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

        double ruleScore = calculateRiskScore(request);
        double aiScore = aiFraudService.getAiScore(request);

        double finalScore = 0.7 * aiScore + 0.3 * ruleScore;

        if(finalScore > 0.8){
            return new FraudResponseDto("BLOCK", finalScore,"High risk detected");
        }
        if( finalScore > 0.5){
            return new FraudResponseDto("FLAG", finalScore,"Suspicious activity");
        }
        return new FraudResponseDto("ALLOW", finalScore,"Low risk detected");
    }
}
