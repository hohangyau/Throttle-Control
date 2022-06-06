package com.example.harry.service;

import org.springframework.stereotype.Service;

import com.example.harry.entity.MarketData;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;


import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.ConsumptionProbe;
import static java.util.concurrent.TimeUnit.*;

@Service
public class MarketDataProcessor {

    private final Map<String, Bucket> bucketCache = new HashMap<>();
    private final Map<String, MarketData> pendingToPublish = new ConcurrentHashMap<>();
    private final List<MarketData> bufferQueue = new ArrayList<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    final Runnable publisher = new Runnable() {
        public void run() { 
            System.out.println(LocalDateTime.now() + " Ready to publish: " + pendingToPublish.size()); 
            for(MarketData data : pendingToPublish.values()){
                System.out.println(data.getUpdateTime());
                publishAggregatedMarketData(data);
            }
            pendingToPublish.clear();
            for(MarketData data : bufferQueue){
                onMessage(data);
            }
            bufferQueue.clear();
        }
    };
    final ScheduledFuture<?> publisherHandle = scheduler.scheduleAtFixedRate(publisher, 1, 1, SECONDS);


    public Bucket resolveBucket(String key) {
        if(!bucketCache.containsKey(key)){
            Bandwidth limit = Bandwidth.simple(100, Duration.ofSeconds(1));
            Bucket bucket = Bucket4j.builder()
            .addLimit(limit)
            .build();
            bucketCache.put(key, bucket);
        }
        return bucketCache.get(key);
    }



    public void onMessage(MarketData marketData){
        //assume data validation is done at api or data receiving level
        Bucket bucket = resolveBucket("default");
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
        if (probe.isConsumed()) {
           onSucess(marketData);
           return;
        }
        System.out.println("exceed limit, will add to buffer queue");
        onFailure(marketData);
    }

    private void onSucess(MarketData marketData){

        //symbol doesn't exist in the pending-to-publish queue, simply add it
        if(!pendingToPublish.containsKey(marketData.getSymbol())){
            pendingToPublish.put(marketData.getSymbol(), marketData);
            return;
        }

        //sysbol already exists in the queue, check update time and replace if current data is more updated
        MarketData exiting = pendingToPublish.get(marketData.getSymbol());
        if(marketData.getUpdateTime().isAfter(exiting.getUpdateTime())){
            pendingToPublish.replace(marketData.getSymbol(), marketData); 
            return;
        }

        //do nothing otherwise

    }

    private void onFailure(MarketData marketData){
        bufferQueue.add(marketData);
    }

    public void publishAggregatedMarketData(MarketData data) {
        // Do Nothing, assume implemented.
    }
        
}
