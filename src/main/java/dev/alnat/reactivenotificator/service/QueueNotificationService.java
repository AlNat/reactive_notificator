package dev.alnat.reactivenotificator.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Created by @author AlNat on 02.02.2023.
 * Licensed by Apache License, Version 2.0
 */
public interface QueueNotificationService {

    Mono<String> saveNotificationQueue(String queueName, Mono<String> body);

    Flux<String> getNotificationQueue(String queueName, Integer count);

}
