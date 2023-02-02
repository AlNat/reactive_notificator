package dev.alnat.reactivenotificator.service;

import dev.alnat.reactivenotificator.config.RedisTestContainerConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Created by @author AlNat on 02.02.2023.
 * Licensed by Apache License, Version 2.0
 */
@SuppressWarnings("ConstantConditions")
@SpringBootTest
@ContextConfiguration(classes = RedisTestContainerConfiguration.class)
@AutoConfigureWebTestClient
class SingleNotificationTest {

    @Autowired
    private WebTestClient webTestClient;


    @Test
    @DisplayName("Get not existing notification")
    void getEmpty() {
        var secondResponse = webTestClient.get().uri("/api/notification/n_2").exchange();
        secondResponse.expectStatus().isNotFound();
    }


    @Test
    @DisplayName("Save and twice get notification")
    void addAndGet() {
        final String data = "some_date";

        var createResponse = webTestClient.post().uri("/api/notification/n_1").bodyValue(data).exchange();
        createResponse.expectStatus().is2xxSuccessful();

        var searchResponse = webTestClient.get().uri("/api/notification/n_1").exchange();
        var body = searchResponse.expectStatus().is2xxSuccessful().expectBody().returnResult();
        Assertions.assertEquals(data, new String(body.getResponseBody()));

        var secondResponse = webTestClient.get().uri("/api/notification/n_1").exchange();
        secondResponse.expectStatus().isNotFound();
    }

}
