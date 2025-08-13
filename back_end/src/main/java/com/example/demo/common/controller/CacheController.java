package com.example.demo.common.controller;

import com.example.demo.user.service.UserService;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.map.LocalMapStats;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cache")
@Slf4j
public class CacheController {

    @Autowired
    private HazelcastInstance hazelcastInstance;

    @Autowired
    private UserService userService;

    // 캐시 상태 확인 API
    @GetMapping("/status")
    public Map<String, Object> getCacheStatus() {
        Map<String, Object> status = new HashMap<>();

        IMap<Object, Object> shortCache = hazelcastInstance.getMap("shortTermCache");
        IMap<Object, Object> longCache = hazelcastInstance.getMap("longTermCache");

        LocalMapStats shortStats = shortCache.getLocalMapStats();
        LocalMapStats longStats = longCache.getLocalMapStats();

        // shortTermCache 정보
        Map<String, Object> shortInfo = new HashMap<>();
        shortInfo.put("size", shortCache.size());
        shortInfo.put("hits", shortStats.getHits());
        // SimpleKey 직렬화 문제 해결 - toString()으로 변환
        shortInfo.put("keys", shortCache.keySet().stream()
                .map(key -> key != null ? key.toString() : "null")
                .collect(Collectors.toList()));

        // longTermCache 정보
        Map<String, Object> longInfo = new HashMap<>();
        longInfo.put("size", longCache.size());
        longInfo.put("hits", longStats.getHits());
        // SimpleKey 직렬화 문제 해결 - toString()으로 변환
        longInfo.put("keys", longCache.keySet().stream()
                .map(key -> key != null ? key.toString() : "null")
                .collect(Collectors.toList()));

        status.put("shortTermCache", shortInfo);
        status.put("longTermCache", longInfo);

        return status;
    }

    // 캐시 클리어 API
    @DeleteMapping("/clear")
    public String clearCache() {
        IMap<Object, Object> cache = hazelcastInstance.getMap("shortTermCache");
        int sizeBefore = cache.size();
        cache.clear();

        return String.format("캐시 클리어 완료. 제거된 항목 수: %d", sizeBefore);
    }
}