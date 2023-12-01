/*----------------------------------------------------------------------------*/
/* Source File:   RATELIMITERCONFIG.JAVA                                      */
/* Copyright (c), 2023 CSoftZ                                                 */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Dec.1/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.polarbookshop.edgeservice.controller.api.v1;

import static org.assertj.core.api.Assertions.assertThat;

import com.polarbookshop.edgeservice.common.config.SecurityConfig;
import com.polarbookshop.edgeservice.domain.User;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Unit Test for UserController
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
@WebFluxTest(UserController.class)
@Import(SecurityConfig.class)
class UserControllerTest {
    private static final String USERNAME = "jon.snow";
    private static final String FIRST_NAME = "Jon";
    private static final String LAST_NAME = "Snow";
    private static final String EMPLOYEE_ROLE = "employee";
    private static final String CUSTOMER_ROLE = "customer";
    private static final String USER_API_V1_PATH = "/api/v1/user";

    @Autowired
    WebTestClient webClient;

    @MockBean
    ReactiveClientRegistrationRepository clientRegistrationRepository;

    @Test
    @DisplayName("Verify when user is not authenticated then return HTTP 401.")
    void whenNotAuthenticatedThen401() {
        webClient
            .get()
            .uri(USER_API_V1_PATH)
            .exchange()
            .expectStatus().isUnauthorized();
    }

    @Test
    @DisplayName("Verify when user is authenticated then return the user information.")
    void whenAuthenticatedThenReturnUser() {

        webClient
            .mutateWith(configureMockOidcLogin(new User(USERNAME, FIRST_NAME, LAST_NAME, List.of(EMPLOYEE_ROLE, CUSTOMER_ROLE))))
            .get()
            .uri(USER_API_V1_PATH)
            .exchange()
            .expectStatus().is2xxSuccessful()
            .expectBody(User.class)
            .value(user -> assertThat(user).isEqualTo(new User(USERNAME, FIRST_NAME, LAST_NAME, List.of(EMPLOYEE_ROLE, CUSTOMER_ROLE))));
    }

    private SecurityMockServerConfigurers.OidcLoginMutator configureMockOidcLogin(User expectedUser) {
        return SecurityMockServerConfigurers
            .mockOidcLogin()
            .idToken(
                builder -> {
                    builder.claim(StandardClaimNames.PREFERRED_USERNAME, expectedUser.username());
                    builder.claim(StandardClaimNames.GIVEN_NAME, expectedUser.firstName());
                    builder.claim(StandardClaimNames.FAMILY_NAME, expectedUser.lastName());
                });
    }
}
