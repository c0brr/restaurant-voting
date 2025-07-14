package ru.erulaev.restaurantvoting.app.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCacheSpecification("maximumSize=100, expireAfterWrite=30m");
        cacheManager.registerCustomCache("users", usersCache());
        cacheManager.registerCustomCache("restaurants", restaurantsCache());
        cacheManager.registerCustomCache("restaurantList", restaurantListCache());
        return cacheManager;
    }

    private Cache<Object, Object> usersCache() {
        return Caffeine.newBuilder()
                .maximumSize(10_000)
                .expireAfterAccess(5, TimeUnit.MINUTES)
                .build();
    }

    private Cache<Object, Object> restaurantsCache() {
        return Caffeine.newBuilder()
                .maximumSize(2_000)
                .expireAfterWrite(1, TimeUnit.DAYS)
                .build();
    }

    private Cache<Object, Object> restaurantListCache() {
        return Caffeine.newBuilder()
                .maximumSize(1)
                .expireAfterWrite(7, TimeUnit.DAYS)
                .build();
    }
}