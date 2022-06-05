package com.example.harry.entity;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MarketData {
    private BigDecimal price;
    private LocalDateTime updateTime;
}
