package dev.alnat.reactivenotificator.api;

import dev.alnat.reactivenotificator.service.QueueNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

/**
 * Created by @author AlNat on 02.02.2023.
 * Licensed by Apache License, Version 2.0
 */
@Component
@RequiredArgsConstructor
public class MessageQueueRouter {

    private final QueueNotificationService service;


    @Bean
    public RouterFunction<ServerResponse> queueRouter() {
        return route(POST("/api/queue/{name}"),
                req -> ok()
                        .body(service.saveNotificationQueue(
                                        req.pathVariable("name"),
                                        req.bodyToMono(String.class)
                                ), String.class
                        ))
                .and(
                        route(GET("/api/queue/{name}"),
                                req -> ok().body(service.getNotificationQueue(req.pathVariable("name"),
                                                req.queryParam("count").map(Integer::parseInt).orElse(1)
                                        ).log(), String.class
                                )
                        )
                );
    }

}
