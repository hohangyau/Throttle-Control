package com.example.harry.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MarketDataController {
    
    @GetMapping(value = "/api/v1/health")
    public String healthCheck(){
        return "Hello Market Data";
    }

    @PostMapping(value = "/api/v1/marketData")
    public String resolve() {

        return "ok";
    }
}
