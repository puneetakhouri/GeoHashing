package com.puneet;

import lombok.Data;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Data
@RedisHash("Store")
public class Store implements Serializable {
    @Id
    private String id;
    private String name;
    private double latitude;
    private double longitude;
}