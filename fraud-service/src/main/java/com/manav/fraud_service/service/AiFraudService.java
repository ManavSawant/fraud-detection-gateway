package com.manav.fraud_service.service;

import com.manav.fraud_service.dto.FraudRequestDto;
import org.springframework.stereotype.Service;

@Service
public class AiFraudService {

    public double getAiScore(FraudRequestDto request){

        double score = 0.2;


        String path = request.getPath().toLowerCase();
        String agent = request.getUserAgent();

        if (agent == null || agent.isEmpty()) {
            score += 0.5;
        }

        if (path.contains("admin") || path.contains("login")) {
            score += 0.4;
        }

        if (path.contains("payment") || path.contains("transfer")) {
            score += 0.5;
        }

        if (request.getIpAddress().startsWith("192")) {
            score += 0.2;
        }

        return Math.min(score, 1.0);
    }
}
