package dev.alnat.reactivenotificator.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * TestContainer Redis for tests
 *
 * Created by @author AlNat on 05.02.2022.
 * Licensed by Apache License, Version 2.0
 */
@Slf4j
@TestConfiguration
public class RedisTestContainerConfiguration {

    private static final String REDIS_DOCKER_NAME = "redis:7.0.8-alpine";

    @Bean
    @Primary
    public ReactiveRedisConnectionFactory reactiveRedisConnectionFactory() {
        final var redis = getRedisContainer();

        log.info("Redis started in test-container");

        return new LettuceConnectionFactory(redis.getHost(), redis.getMappedPort(6379));
    }

    private GenericContainer<?> getRedisContainer() {
        GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse(REDIS_DOCKER_NAME)).withExposedPorts(6379);
        redis.start();
        return redis;
    }

}
