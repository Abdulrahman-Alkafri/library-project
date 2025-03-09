package org.example.library.Config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Primary;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean(name = "defaultCacheManager")
    @Primary // this bean the default one for injection
    public CaffeineCacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(100));
        cacheManager.setCacheNames(List.of("books", "patrons"));
        return cacheManager;
    }

    @Bean(name = "booksCacheManager")
    public CaffeineCacheManager booksCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("books");
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(15, TimeUnit.MINUTES)
                .maximumSize(200));
        return cacheManager;
    }

    @Bean(name = "patronsCacheManager")
    public CaffeineCacheManager patronsCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("patrons");
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(20, TimeUnit.MINUTES)
                .maximumSize(150));
        return cacheManager;
    }
}