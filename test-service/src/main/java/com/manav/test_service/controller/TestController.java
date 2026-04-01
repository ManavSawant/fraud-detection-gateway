package com.manav.test_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello Test Service";
    }

    @GetMapping("/admin")
    public String admin(@RequestHeader(value = "X-Fraud-Flag", required = false) String flag) {

        System.out.println("X-Fraud-Flag header received: " + flag);

        return "Admin endpoint";
    }
}
