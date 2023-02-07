package dev.alnat.reactivenotificator.api;

import dev.alnat.reactivenotificator.service.QueueNotificationService;
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
    @RouterOperations({
            @RouterOperation(
                    path = "/api/queue/{queueName}", method = RequestMethod.POST,
                    beanClass = QueueNotificationService.class, beanMethod = "saveNotificationQueue",
                    operation = @Operation(
                            operationId = "saveNotificationQueue",
                            summary = "Save new notification in queue with name",
                            tags = {"NotificationQueue"},
                            parameters = {
                                    @Parameter(in = ParameterIn.PATH, name = "queueName", description = "Queue name", required = true)
                            },
                            requestBody = @RequestBody(description = "Notification data"),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Successfully saved in queue"),
                                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                                    @ApiResponse(responseCode = "500", description = "Something went wrong")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/queue/{queueName}", method = RequestMethod.GET,
                    beanClass = QueueNotificationService.class, beanMethod = "getNotificationQueue",
                    operation = @Operation(
                            operationId = "getNotificationQueue",
                            summary = "Get notification from queue by queue name",
                            tags = {"NotificationQueue"},
                            parameters = {
                                    @Parameter(in = ParameterIn.PATH, name = "queueName", description = "Queue name", required = true),
                                    @Parameter(in = ParameterIn.QUERY, name = "count", description = "Count of fetching notifications", required = false)
                            },
                            responses = {
                                    @ApiResponse(responseCode = "200",
                                            description = "Successfully fetched data from queue, data in body.\n" +
                                                "If queue is empty or not exist -- it will be empty",
                                            content = @Content(schema = @Schema(implementation = String.class))),
                                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                                    @ApiResponse(responseCode = "500", description = "Something went wrong")
                            }
                    )
            ),
    })
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
