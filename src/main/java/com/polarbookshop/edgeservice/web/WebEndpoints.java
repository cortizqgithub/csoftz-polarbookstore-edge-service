/*----------------------------------------------------------------------------*/
/* Source File:   WEBENDPOINTS.JAVA                                           */
/* Copyright (c), 2023 CSoftZ                                                 */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Nov.10/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.polarbookshop.edgeservice.web;

import static com.polarbookshop.edgeservice.common.consts.GlobalConstants.EMPTY_STR;
import static com.polarbookshop.edgeservice.common.consts.GlobalConstants.ROUTE_CATALOG_FALLBACK;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * Reactive Routers.
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
@Configuration
public class WebEndpoints {

    @Bean
    public RouterFunction<ServerResponse> routerFunction() {
        return RouterFunctions.route()
            .GET(ROUTE_CATALOG_FALLBACK, request -> ServerResponse.ok().body(Mono.just(EMPTY_STR), String.class))
            .POST(ROUTE_CATALOG_FALLBACK, request -> ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE).build())
            .build();
    }
}
