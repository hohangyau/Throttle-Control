package com.example.harry.entity;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

public class MarketData {
    @Getter @Setter private String symbol;
    @Getter @Setter private BigDecimal bid;
    @Getter @Setter private BigDecimal ask;
    @Getter @Setter private BigDecimal last;
    @Getter @Setter private LocalDateTime updateTime;

    @Override public String toString() {
        return String.format("%s (bid: %s,ask: %s, last: %s, updateTime %s )", symbol, bid, ask, last, updateTime);
      }
}
