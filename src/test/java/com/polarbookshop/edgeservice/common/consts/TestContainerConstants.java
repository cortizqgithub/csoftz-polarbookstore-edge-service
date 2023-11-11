/*----------------------------------------------------------------------------*/
/* Source File:   TESTCONTAINERCONSTANTS.JAVA                                 */
/* Copyright (c), 2023 CSoftZ                                                 */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Nov.11/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.polarbookshop.edgeservice.common.consts;

/**
 * Defines common TestContainer Constants.
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
public class TestContainerConstants {

    public static final String TEST_SPRING_DATA_REDIS_HOST = "spring.data.redis.host";
    public static final String REDIS_DOCKER_VERSION = "redis:latest";

    public static final int REDIS_PORT = 6379;
    public static final String TEST_SPRING_DATA_REDIS_PORT = "spring.data.redis.port";

    /**
     * This is a utility class thus we must avoid to instantiate this.
     */
    private TestContainerConstants() {
    }
}
