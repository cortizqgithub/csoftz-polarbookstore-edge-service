/*----------------------------------------------------------------------------*/
/* Source File:   SECURITYCONFIG.JAVA                                         */
/* Copyright (c), 2023 CSoftZ                                                 */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Nov.24/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.polarbookshop.edgeservice.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.security.web.server.csrf.CookieServerCsrfTokenRepository;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.security.web.server.csrf.XorServerCsrfTokenRequestAttributeHandler;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;

/**
 * Creates configureation bean for {@code Spring Security}.
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private static final String PUBLIC_PATH_ROOT = "/";
    private static final String PUBLIC_CSS_RESOURCES = "/*.css";
    private static final String PUBLIC_JS_RESOURCES = "/*.js";
    private static final String PUBLIC_FAVICON_ICO = "/favicon.ico";
    private static final String API_V1_BOOKS_PATH = "/api/v1/books/**";
    private static final String API_V1_HOME_PATH = "/api/v1/home";
    private static final String LOGOUT_REDIRECT_BASE_URL = "{baseUrl}";

    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http,
                                                     ReactiveClientRegistrationRepository clientRegistrationRepository) {
        return
            http.authorizeExchange(
                    exchange ->
                        exchange
                            .pathMatchers(PUBLIC_PATH_ROOT, PUBLIC_CSS_RESOURCES, PUBLIC_JS_RESOURCES, PUBLIC_FAVICON_ICO).permitAll()
                            .pathMatchers(HttpMethod.GET, API_V1_BOOKS_PATH, API_V1_HOME_PATH).permitAll()
                            .anyExchange().authenticated())
                .exceptionHandling(exceptionHandling -> exceptionHandling.authenticationEntryPoint(new HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED)))
                .oauth2Login(Customizer.withDefaults())
                .logout(logout -> logout.logoutSuccessHandler(oidcLogoutSuccessHandler(clientRegistrationRepository)))
                .csrf(
                    csrf ->
                        csrf.csrfTokenRepository(CookieServerCsrfTokenRepository.withHttpOnlyFalse())
                            .csrfTokenRequestHandler(new XorServerCsrfTokenRequestAttributeHandler()::handle))
                .build();
    }

    @Bean
    WebFilter csrfWebFilter() {
        // Required because of https://github.com/spring-projects/spring-security/issues/5766
        return (exchange, chain) -> {
            exchange.getResponse().beforeCommit(() -> Mono.defer(() -> {
                var csrfToken = exchange.<Mono<CsrfToken>>getAttribute(CsrfToken.class.getName());

                return csrfToken != null ? csrfToken.then() : Mono.empty();
            }));
            return chain.filter(exchange);
        };
    }

    private ServerLogoutSuccessHandler oidcLogoutSuccessHandler(ReactiveClientRegistrationRepository clientRegistrationRepository) {
        var oidcLogoutSuccessHandler = new OidcClientInitiatedServerLogoutSuccessHandler(clientRegistrationRepository);

        oidcLogoutSuccessHandler.setPostLogoutRedirectUri(LOGOUT_REDIRECT_BASE_URL);

        return oidcLogoutSuccessHandler;
    }
}
