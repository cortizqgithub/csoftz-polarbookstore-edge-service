/*----------------------------------------------------------------------------*/
/* Source File:   APPLICATIONTESTS.JAVA                                       */
/* Copyright (c), 2023 CSoftZ                                                 */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Nov.04/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.polarbookshop.edgeservice;

import static com.polarbookshop.edgeservice.common.consts.TestContainerConstants.REDIS_DOCKER_VERSION;
import static com.polarbookshop.edgeservice.common.consts.TestContainerConstants.REDIS_PORT;
import static com.polarbookshop.edgeservice.common.consts.TestContainerConstants.TEST_SPRING_DATA_REDIS_HOST;
import static com.polarbookshop.edgeservice.common.consts.TestContainerConstants.TEST_SPRING_DATA_REDIS_PORT;

import com.polarbookshop.edgeservice.common.consts.TestContainerConstants;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
/**
 * Unit tests for Application class.
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
@SpringBootTest
@Testcontainers
class ApplicationTests {
    @Container
    static GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse(REDIS_DOCKER_VERSION)).withExposedPorts(REDIS_PORT);

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add(TEST_SPRING_DATA_REDIS_HOST, () -> redis.getHost());
        registry.add(TEST_SPRING_DATA_REDIS_PORT, () -> redis.getMappedPort(REDIS_PORT));
    }

    /**
     * Load Spring Boot contexts to validate it can run the application.
     * Somehow, as some dependencies such as Backend Data Service or another component, it
     * is required that we use TestContainers in order to boostrap that dependency.
     * As this backend service requires Redis, we use a docker container to
     * fill the requirement, and actually use the same server as in prod. Here, we
     * use TestContainers to wire that context test. NOTE: Other containers should be
     * added when required.
     */
    @Test
    void contextLoads() {
    }
}
