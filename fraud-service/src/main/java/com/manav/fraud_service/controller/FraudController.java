package com.manav.fraud_service.controller;

import com.manav.fraud_service.dto.FraudRequestDto;
import com.manav.fraud_service.dto.FraudResponseDto;
import com.manav.fraud_service.service.FraudAnalysisService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fraud")
@RequiredArgsConstructor
public class FraudController {
    private final FraudAnalysisService fraudAnalysisService;

    @PostMapping("/check")
    public FraudResponseDto checkFraud(@RequestBody FraudRequestDto request){
        return fraudAnalysisService.analyze(request);
    }
}
