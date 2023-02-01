package dev.alnat.reactivenotificator.config;

import dev.alnat.reactivenotificator.model.SingleNotification;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Created by @author AlNat on 01.02.2023.
 * Licensed by Apache License, Version 2.0
 */
@Configuration
public class RedisConfiguration {

    @Bean
    public ReactiveRedisTemplate<String, SingleNotification> reactiveRedisTemplate(
            ReactiveRedisConnectionFactory factory) {
        Jackson2JsonRedisSerializer<SingleNotification> valueSerializer = new Jackson2JsonRedisSerializer<>(SingleNotification.class);

        RedisSerializationContext.RedisSerializationContextBuilder<String, SingleNotification> builder =
                RedisSerializationContext.newSerializationContext(new StringRedisSerializer());

        RedisSerializationContext<String, SingleNotification> context = builder.value(valueSerializer).build();
        return new ReactiveRedisTemplate<>(factory, context);
    }

}
