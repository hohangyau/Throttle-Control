package com.example.harry.controller;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.harry.entity.MarketData;
import com.example.harry.service.MarketDataProcessor;

@RestController
public class MarketDataController {
    
    @Autowired
    MarketDataProcessor marketDataProcessor;

    @GetMapping(value = "/api/v1/health")
    public String healthCheck() throws InterruptedException{
        MarketData data = new MarketData();
        data.setBid(new BigDecimal(100));
        data.setAsk(new BigDecimal(100));
        data.setLast(new BigDecimal(100));
        data.setUpdateTime(LocalDateTime.now());
        data.setSymbol("btc");
        System.out.println("on message " + LocalDateTime.now().toString());
        return marketDataProcessor.onMessage(data);
      //  return "Hello Market Data";
    }

    @PostMapping(value = "/api/v1/marketData")
    public String resolve() {

        return "ok";
    }
}
