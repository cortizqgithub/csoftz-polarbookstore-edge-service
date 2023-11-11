/*----------------------------------------------------------------------------*/
/* Source File:   RATELIMITERCONFIG.JAVA                                      */
/* Copyright (c), 2023 CSoftZ                                                 */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Nov.11/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.polarbookshop.edgeservice.common.config;

import static com.polarbookshop.edgeservice.common.consts.GlobalConstants.KEY_RESOLVE_ANONYMOUS;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

/**
 * Defining a strategy to resolve the bucket to use per request.
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
@Configuration
public class RateLimiterConfig {

    /**
     * Spring Cloud Gateway Redis integration requires a key resolver bean to determine which bucket to use for each request.
     * By default uses the currently authenticateduser in Spring Security. But as project does not
     * have it set up we use the following definition.
     * @return annonymous key resolver name.
     */
    @Bean
    public KeyResolver keyResolver() {
        return exchange -> Mono.just(KEY_RESOLVE_ANONYMOUS);
    }
}
