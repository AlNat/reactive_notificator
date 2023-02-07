package dev.alnat.reactivenotificator.api;

import dev.alnat.reactivenotificator.service.SingleNotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
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
    @RouterOperations({
            @RouterOperation(
                    path = "/api/notification/{key}", method = RequestMethod.POST,
                    beanClass = SingleNotificationService.class, beanMethod = "saveNotification",
                    operation = @Operation(
                            operationId = "saveNewNotification",
                            summary = "Save new notification",
                            tags = {"Notification"},
                            parameters = {
                                    @Parameter(in = ParameterIn.PATH, name = "key", description = "Notification key", required = true),
                                    @Parameter(in = ParameterIn.QUERY, name = "ttl", description = "TTL of the notification")
                            },
                            requestBody = @RequestBody(description = "Notification data"),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Successfully saved"),
                                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                                    @ApiResponse(responseCode = "500", description = "Something went wrong")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/notification/{key}", method = RequestMethod.GET,
                    beanClass = SingleNotificationService.class, beanMethod = "getNotification",
                    operation = @Operation(
                            operationId = "getNewNotification",
                            summary = "Get notification by key",
                            tags = {"Notification"},
                            parameters = {
                                    @Parameter(in = ParameterIn.PATH, name = "key", description = "Notification key", required = true)
                            },
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Successfully found notification, data in body",
                                            content = @Content(schema = @Schema(implementation = String.class))),
                                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                                    @ApiResponse(responseCode = "404", description = "Not found notification or it's too late"),
                                    @ApiResponse(responseCode = "500", description = "Something went wrong")
                            }
                    )
            ),
    })
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
