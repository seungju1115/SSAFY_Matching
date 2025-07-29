package com.example.demo.util;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.hazelcast.config.*;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class HazelcastConfig {
    @Bean
    public Config hazelcastConfig() {
        Config config = new Config();
        config.setInstanceName("hazelcast-instance");

        // 네트워크 설정
        NetworkConfig networkConfig = config.getNetworkConfig();
        networkConfig.setPort(5701)
                .setPortAutoIncrement(true);

        // 클러스터 멤버 발견 설정
        JoinConfig joinConfig = networkConfig.getJoin();
        joinConfig.getMulticastConfig()
                .setEnabled(true)
                .setMulticastGroup("239.1.2.3")
                .setMulticastPort(54327);

        // TCP/IP는 비활성화 (멀티캐스트 사용)
        joinConfig.getTcpIpConfig().setEnabled(false);

        // 캐시별 설정
        MapConfig longTermConfig = config.getMapConfig("longTermCache");
        longTermConfig.setTimeToLiveSeconds(3600);
        longTermConfig.setMapStoreConfig();
        new MapStoreConfig();
        mapStoreConfig.set
        return config;
    }


    @Bean()
    public CacheManager longTermCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.HOURS)  // 1시간 후 만료
                .maximumSize(5));                   // 최대 100개 캐시
        return cacheManager;
    }

    @Bean("shortTermCache")
    public CacheManager shortTermCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(5, TimeUnit.MINUTES)  // 10분 후 만료
                .maximumSize(500));
        return cacheManager;
    }
}
