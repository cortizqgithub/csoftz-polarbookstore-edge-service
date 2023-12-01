/*----------------------------------------------------------------------------*/
/* Source File:   USERCONTROLLER.JAVA                                         */
/* Copyright (c), 2023 CSoftZ                                                 */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Nov.28/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.polarbookshop.edgeservice.controller.api.v1;

import com.polarbookshop.edgeservice.domain.User;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Exposes as a REST Api the info about the logged in User.
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 * @see User
 */
@RestController
public class UserController {

    /**
     * Retrieves information about the logged-in user from the Authorization Server.
     * GET /api/v1/user
     * <b>NOTE:</b> Here we inject the OidcUser via the annotation, but you could as well use the
     * the following code to accomplish the same.
     * <pre>{@code
     *     @GetMapping("/api/v1/user")
     *     public Mono<User> getUser() {
     *         return ReactiveSecurityContextHolder.getContext()
     *             .map(SecurityContext::getAuthentication)
     *             .map(authentication ->
     *                 (OidcUser) authentication.getPrincipal())
     *             .map(oidcUser ->
     *                 new User(
     *                     oidcUser.getPreferredUsername(),
     *                     oidcUser.getGivenName(),
     *                     oidcUser.getFamilyName(),
     *                     List.of("employee", "customer")
     *                 )
     *             );
     *     }
     * }</pre>
     *
     * @param oidcUser
     * @return Authenticated user (principal) info.
     * @see OidcUser
     */
    @GetMapping("/api/v1/user")
    public Mono<User> getUser(@AuthenticationPrincipal OidcUser oidcUser) {
        var user = new User(
            oidcUser.getPreferredUsername(),
            oidcUser.getGivenName(),
            oidcUser.getFamilyName(),
            List.of("employee", "customer")
        );

        return Mono.just(user);
    }
}
