package com.example.taskmanagementsystem.configurations;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@EnableCaching
@Configuration
public class CacheConfig {

    @Bean("defaultCache")
    @Primary
    public CacheManager cacheManager(){
        System.out.println("Cache initialization");
        return new ConcurrentMapCacheManager("taskInfo");
    }
}
