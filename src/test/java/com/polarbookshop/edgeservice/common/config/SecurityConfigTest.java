/*----------------------------------------------------------------------------*/
/* Source File:   TESTCONTAINERCONSTANTS.JAVA                                 */
/* Copyright (c), 2023 CSoftZ                                                 */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Dec.1/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.polarbookshop.edgeservice.common.config;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

/**
 * Unit Test for SecurityConfig
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
@WebFluxTest
@Import(SecurityConfig.class)
class SecurityConfigTest {
    private static final String LOGOUT_PATH = "/logout";
    private static final String POLARBOOKSHOP_COM_AUTH = "https://sso.polarbookshop.com/auth";
    private static final String POLARBOOKSHOP_COM_TOKEN = "https://sso.polarbookshop.com/token";
    private static final String POLARBOOKSHOP_COM = "https://polarbookshop.com";
    private static final String AUTH_SERVER_REGISTRATION_ID = "test";

    @Autowired
    WebTestClient webClient;

    @MockBean
    ReactiveClientRegistrationRepository clientRegistrationRepository;

    @Test
    @DisplayName("Verify we can successfully make a logout. HTTP 403 returned.")
    void whenLogoutNotAuthenticatedAndNoCsrfTokenThen403() {
        webClient
            .post()
            .uri(LOGOUT_PATH)
            .exchange()
            .expectStatus().isForbidden();
    }

    @Test
    @DisplayName("Verify when trying the logout flow as authenticated but no CSRF token. HTTP 403 returned.")
    void whenLogoutAuthenticatedAndNoCsrfTokenThen403() {
        webClient
            .mutateWith(SecurityMockServerConfigurers.mockOidcLogin())
            .post()
            .uri(LOGOUT_PATH)
            .exchange()
            .expectStatus().isForbidden();
    }

    @Test
    @DisplayName("Verify when trying the logout flow as authenticated with CSRF token. HTTP 302 returned")
    void whenLogoutAuthenticatedAndWithCsrfTokenThen302() {
        when(clientRegistrationRepository.findByRegistrationId(AUTH_SERVER_REGISTRATION_ID)).thenReturn(Mono.just(testClientRegistration()));

        webClient
            .mutateWith(SecurityMockServerConfigurers.mockOidcLogin())
            .mutateWith(SecurityMockServerConfigurers.csrf())
            .post()
            .uri(LOGOUT_PATH)
            .exchange()
            .expectStatus().isFound();
    }

    private ClientRegistration testClientRegistration() {
        return ClientRegistration.withRegistrationId(AUTH_SERVER_REGISTRATION_ID)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .clientId(AUTH_SERVER_REGISTRATION_ID)
            .authorizationUri(POLARBOOKSHOP_COM_AUTH)
            .tokenUri(POLARBOOKSHOP_COM_TOKEN)
            .redirectUri(POLARBOOKSHOP_COM)
            .build();
    }
}
