package com.puneet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StoreService {

    private static final String STORE_KEY = "stores";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public Store addStore(Store store) {
        redisTemplate.opsForGeo().add(STORE_KEY, new Point(store.getLongitude(), store.getLatitude()), store.getId());
        redisTemplate.opsForHash().put(STORE_KEY + ":hash", store.getId(), store);
        return store;
    }

    public boolean isWithinDistance(String storeId, double latitude, double longitude, double maxDistance) {
        Distance distance = redisTemplate.opsForGeo().distance(STORE_KEY, storeId, new Point(longitude, latitude), Metrics.NEUTRAL);
        return distance != null && distance.getValue() <= maxDistance;
    }

    public List<Store> findNearbyStores(double latitude, double longitude, double maxDistance) {
        Circle circle = new Circle(new Point(longitude, latitude), new Distance(maxDistance, Metrics.KILOMETERS));
        GeoResults<RedisGeoCommands.GeoLocation<Object>> results = redisTemplate.opsForGeo().radius(STORE_KEY, circle);

        return results.getContent().stream()
                .map(result -> (Store) redisTemplate.opsForHash().get(STORE_KEY + ":hash", result.getContent().getName()))
                .collect(Collectors.toList());
    }
}