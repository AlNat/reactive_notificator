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
class QueueTest {

    @Autowired
    private WebTestClient webTestClient;


    @Test
    @DisplayName("Get from empty queue")
    void getEmpty() {
        var secondResponse = webTestClient.get().uri("/api/queue/q_1").exchange();
        secondResponse.expectStatus().isOk();
    }


    @Test
    @DisplayName("Save and twice get notification from queue")
    void addAndGet() {
        final String data = "some_date";

        var createResponse = webTestClient.post().uri("/api/queue/q_2").bodyValue(data).exchange();
        createResponse.expectStatus().is2xxSuccessful();

        var searchResponse = webTestClient.get().uri("/api/queue/q_2").exchange();
        var body = searchResponse.expectStatus().is2xxSuccessful().expectBody().returnResult();
        Assertions.assertEquals(data, new String(body.getResponseBody()).trim());

        var secondResponse = webTestClient.get().uri("/api/queue/q_2").exchange();
        secondResponse.expectStatus().isOk();
    }


    @Test
    @DisplayName("Save twice and twice get notification from queue")
    void addAndGetTwice() {
        final String data = "some_date";

        var createResponse = webTestClient.post().uri("/api/queue/q_3").bodyValue(data).exchange();
        createResponse.expectStatus().is2xxSuccessful();

        var createResponse2 = webTestClient.post().uri("/api/queue/q_3").bodyValue(data).exchange();
        createResponse2.expectStatus().is2xxSuccessful();

        var searchResponse = webTestClient.get().uri("/api/queue/q_3").exchange();
        var body = searchResponse.expectStatus().is2xxSuccessful().expectBody().returnResult();
        Assertions.assertEquals(data, new String(body.getResponseBody()).trim());

        var secondResponse = webTestClient.get().uri("/api/queue/q_3").exchange();
        var secondBody = secondResponse.expectStatus().is2xxSuccessful().expectBody().returnResult();
        Assertions.assertEquals(data, new String(secondBody.getResponseBody()).trim());
    }


    @Test
    @DisplayName("Save 6 notification and twice get of 3 notifications from queue")
    void addBulkAndGetBuld() {
        final String data = "some_date";

        for (int i = 0; i < 6; i++) {
            var createResponse = webTestClient.post().uri("/api/queue/q_4")
                    .bodyValue(data + (i + 1)).exchange();
            createResponse.expectStatus().is2xxSuccessful();
        }

        var searchResponse = webTestClient.get().uri("/api/queue/q_4?count=3").exchange();
        var b = searchResponse.expectStatus().is2xxSuccessful().expectBody().returnResult();
        var body = new String(b.getResponseBody());

        Assertions.assertTrue(body.contains("1"));
        Assertions.assertTrue(body.contains("2"));
        Assertions.assertTrue(body.contains("3"));

        var secondResponse = webTestClient.get().uri("/api/queue/q_4?count=3").exchange();
        var b2 = secondResponse.expectStatus().is2xxSuccessful().expectBody().returnResult();
        var secondBody = new String(b2.getResponseBody());

        Assertions.assertTrue(secondBody.contains("4"));
        Assertions.assertTrue(secondBody.contains("5"));
        Assertions.assertTrue(secondBody.contains("6"));
    }

}
