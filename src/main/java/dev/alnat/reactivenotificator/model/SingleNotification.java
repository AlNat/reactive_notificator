package dev.alnat.reactivenotificator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.io.Serial;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * Created by @author AlNat on 01.02.2023.
 * Licensed by Apache License, Version 2.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "SingleNotification")
public class SingleNotification implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private String key;

    private String value;

    @TimeToLive(unit = TimeUnit.SECONDS)
    private Long ttl;

}
