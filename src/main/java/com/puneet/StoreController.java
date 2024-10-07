package com.puneet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stores")
public class StoreController {

    @Autowired
    private StoreService storeService;

    @PostMapping
    public ResponseEntity<Store> addStore(@RequestBody Store store) {
        return ResponseEntity.ok(storeService.addStore(store));
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> checkDistance(
            @RequestParam String storeId,
            @RequestParam double latitude,
            @RequestParam double longitude) {
        boolean isWithin = storeService.isWithinDistance(storeId, latitude, longitude, 30);
        return ResponseEntity.ok(isWithin);
    }

    @GetMapping("/nearby")
    public ResponseEntity<List<Store>> getNearbyStores(
            @RequestParam double latitude,
            @RequestParam double longitude) {
        List<Store> nearbyStores = storeService.findNearbyStores(latitude, longitude, 100);
        return ResponseEntity.ok(nearbyStores);
    }
}
