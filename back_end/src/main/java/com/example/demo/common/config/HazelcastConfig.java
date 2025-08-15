package com.example.demo.common.config;

import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.spring.cache.HazelcastCacheManager;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class HazelcastConfig {
    @Bean
    @Profile("local")
    public Config hazelcastConfigLocal() {
        Config config = new Config();
        config.setInstanceName("hazelcast-local");

        // 로컬에서는 멀티캐스트 사용 (간단함)
        NetworkConfig networkConfig = config.getNetworkConfig();
        networkConfig.setPort(5701);

        JoinConfig joinConfig = networkConfig.getJoin();
        joinConfig.getMulticastConfig().setEnabled(true);  // 멀티캐스트 활성화
        joinConfig.getTcpIpConfig().setEnabled(false);     // TCP-IP 비활성화

        MapConfig longTermConfig = new MapConfig();
        longTermConfig.setName("longTermCache");
        longTermConfig.setTimeToLiveSeconds(3600);

        EvictionConfig evictionConfig = new EvictionConfig();
        evictionConfig.setEvictionPolicy(EvictionPolicy.LRU);
        evictionConfig.setMaxSizePolicy(MaxSizePolicy.PER_NODE);
        evictionConfig.setSize(300);
        longTermConfig.setEvictionConfig(evictionConfig);
        config.addMapConfig(longTermConfig);

        MapConfig shortTermConfig = new MapConfig();
        shortTermConfig.setName("shortTermCache");
        shortTermConfig.setTimeToLiveSeconds(30);

        EvictionConfig evictionConfig2 = new EvictionConfig();
        evictionConfig2.setEvictionPolicy(EvictionPolicy.LRU);
        evictionConfig2.setMaxSizePolicy(MaxSizePolicy.PER_NODE);
        evictionConfig2.setSize(10);
        shortTermConfig.setEvictionConfig(evictionConfig2);
        config.addMapConfig(shortTermConfig);

        config.getMetricsConfig().setEnabled(true);
        return config;
    }

    @Bean
    @Profile("prod") // 추후 nginx까지 달렸을때 사용
    public Config hazelcastConfigProd() {
        Config config = new Config();
        config.setInstanceName("hazelcast-instance");

        NetworkConfig networkConfig = config.getNetworkConfig();
        networkConfig.setPort(5701);

        JoinConfig joinConfig = networkConfig.getJoin();
        joinConfig.getMulticastConfig().setEnabled(false);

        TcpIpConfig tcpIpConfig = joinConfig.getTcpIpConfig();
        tcpIpConfig.setEnabled(true);

        // Docker 서비스명 또는 컨테이너 IP 사용
        tcpIpConfig.addMember("spring-backend1:5701")    // 서비스명
                .addMember("spring-backend2:5701")    // 서비스명
                .addMember("spring-backend3:5701");

        // 캐시별 설정
        MapConfig longTermConfig = new MapConfig();
        longTermConfig.setName("longTermCache");
        longTermConfig.setTimeToLiveSeconds(3600);

        EvictionConfig evictionConfig = new EvictionConfig();
        evictionConfig.setEvictionPolicy(EvictionPolicy.LRU);
        evictionConfig.setMaxSizePolicy(MaxSizePolicy.PER_NODE);
        evictionConfig.setSize(300);
        longTermConfig.setEvictionConfig(evictionConfig);
        config.addMapConfig(longTermConfig);

        MapConfig shortTermConfig = new MapConfig();
        shortTermConfig.setName("shortTermCache");
        shortTermConfig.setTimeToLiveSeconds(30);

        EvictionConfig evictionConfig2 = new EvictionConfig();
        evictionConfig2.setEvictionPolicy(EvictionPolicy.LRU);
        evictionConfig2.setMaxSizePolicy(MaxSizePolicy.PER_NODE);
        evictionConfig2.setSize(10);
        shortTermConfig.setEvictionConfig(evictionConfig2);
        config.addMapConfig(shortTermConfig);

        return config;
    }

    @Bean
    public HazelcastInstance hazelcastInstance(Config config) {
        return Hazelcast.newHazelcastInstance(config);
    }

    @Bean
    public CacheManager cacheManager(HazelcastInstance hazelcastInstance) {
        return new HazelcastCacheManager(hazelcastInstance);
    }

}
