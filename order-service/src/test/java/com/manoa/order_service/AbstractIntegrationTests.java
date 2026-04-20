package com.manoa.order_service;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AbstractIntegrationTests {

    @LocalServerPort // This is going to bind Random port to this variable
    int port;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
    }
}
