package dev.alnat.reactivenotificator.api;

import dev.alnat.reactivenotificator.service.SingleNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.notFound;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

/**
 * Created by @author AlNat on 01.02.2023.
 * Licensed by Apache License, Version 2.0
 */
@Component
@RequiredArgsConstructor
public class SingleMessageRouter {

    private final SingleNotificationService service;

    @Bean
    public RouterFunction<ServerResponse> singleNotificationRoutes() {
        return route(POST("/api/notification/{key}"),
                req -> ok()
                        .body(service.saveNotification(
                                req.pathVariable("key"),
                                req.queryParam("ttl"),
                                req.bodyToMono(String.class)
                                ), String.class
                        ))
                .and(
                        route(GET("/api/notification/{key}"),
                                req -> service.getNotification(req.pathVariable("key"))
                                        .flatMap(n -> ok().bodyValue(n))
                                        .switchIfEmpty(notFound().build())
                                        .log()
                        )
                );
    }

}
