package com.manav.getaway.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FraudRequest {

    private String ipAddress;
    private String userAgent;
    private String path;

}
