package com.example.harry.service;

import org.springframework.stereotype.Service;

import com.example.harry.entity.MarketData;

import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.Refill;
import static java.util.concurrent.TimeUnit.*;

@Service
public class MarketDataProcessor {

    private final Map<String, Bucket> cache = new HashMap<>();
    private final Map<String, MarketData> pendingToPublish = new HashMap<>();
    public Bucket resolveBucket(String key) {
        if(!cache.containsKey(key)){
            Bandwidth limit = Bandwidth.simple(1, Duration.ofSeconds(1));
            Bucket bucket = Bucket4j.builder()
            .addLimit(limit)
            .build();
            cache.put(key, bucket);
        }
        return cache.get(key);
    }

    private Bucket createNewBucket() {
        long overdraft = 50;
        Refill refill = Refill.greedy(1, Duration.ofMinutes(1));
        Bandwidth limit = Bandwidth.classic(overdraft, refill);
        return Bucket4j.builder().addLimit(limit).build();
   }

    public String onMessage(MarketData marketdata) throws InterruptedException {

        Bucket bucket = resolveBucket("default");
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
        if (probe.isConsumed()) {
            if(pendingToPublish.containsKey(marketdata.getSymbol())){

            }
            onSucess(marketdata);
           return String.format("%d",bucket.getAvailableTokens());
        }
        long waitForRefill = probe.getNanosToWaitForRefill();
         return "exceed limit, please wait until: " + waitForRefill;
        
 

      //  onFailure(data);

       // return "";
    }

    private String onSucess(MarketData marketData){
        publishAggregatedMarketData(marketData);
        return marketData.toString();


        //return String.format("%d",myBucket.getAvailableTokens());
    }

    private String onFailure(MarketData marketData){
        return "exceed limit";
    }

    public void publishAggregatedMarketData(MarketData data) {
        // Do Nothing, assume implemented.
    }
        
}
