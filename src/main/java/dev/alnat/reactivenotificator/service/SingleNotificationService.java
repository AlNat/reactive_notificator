package dev.alnat.reactivenotificator.service;

import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * Created by @author AlNat on 01.02.2023.
 * Licensed by Apache License, Version 2.0
 */
public interface SingleNotificationService {

    Mono<String> saveNotification(String key, Optional<String> ttl, Mono<String> body);

    Mono<String> getNotification(String key);

}
