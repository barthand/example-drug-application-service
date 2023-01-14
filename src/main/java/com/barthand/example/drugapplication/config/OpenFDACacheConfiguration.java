package com.barthand.example.drugapplication.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableCaching
@EnableScheduling
@Slf4j
@ConditionalOnProperty(value = "openfda.enable-cache", havingValue = "true")
public class OpenFDACacheConfiguration {

    public static final String OPENFDA_DRUGS_CACHE = "openfda-drugs-cache";

    @CacheEvict(allEntries = true, value = {OPENFDA_DRUGS_CACHE})
    @Scheduled(cron = "${openfda.cron-expression-for-cache-eviction:0 0 * * * *}")
    public void reportCacheEvict() {
        log.info("Evicted cache for {}", OPENFDA_DRUGS_CACHE);
    }

}
