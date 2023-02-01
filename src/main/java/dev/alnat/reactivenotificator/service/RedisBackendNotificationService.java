package dev.alnat.reactivenotificator.service;

import dev.alnat.reactivenotificator.model.SingleNotification;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * Created by @author AlNat on 01.02.2023.
 * Licensed by Apache License, Version 2.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RedisBackendNotificationService implements SingleNotificationService {

    @Value("${custom.default-notification-ttl}")
    private Long defaultTTL;

    private final ReactiveRedisTemplate<String, SingleNotification> reactiveRedisTemplate;
    private final MeterRegistry registry;

    @Override
    public Mono<String> saveNotification(final String key,
                                         final Optional<String> ttl,
                                         final Mono<String> body) {
        return body
                .map(b -> new SingleNotification(key, b, ttl.map(Long::valueOf).orElse(defaultTTL)))
                .doOnNext(n -> registry.counter("NEW_SAVES").increment())
                .flatMap(notification -> reactiveRedisTemplate.opsForSet().add(key, notification))
                .map(String::valueOf)
                .log()
                .doOnError(e -> log.error("", e));
    }

    @Override
    public Mono<String> getNotification(final String key) {
        return reactiveRedisTemplate.opsForSet()
                .pop(key)
                .doOnNext(n -> registry.counter("GET").increment())
                .map(SingleNotification::getValue)
                .log()
                .doOnError(e -> log.error("", e));
    }

}
