/*----------------------------------------------------------------------------*/
/* Source File:   USER.JAVA                                                   */
/* Copyright (c), 2023 CSoftZ                                                 */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Nov.28/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.polarbookshop.edgeservice.domain;

import java.util.List;

/**
 * Expose Authenticated user info for the Polar BookStore.
 *
 * @param username  Refers to the alias name for the User.
 * @param firstName Refers to the given name for the User.
 * @param lastName  Refers to the given surname for the User.
 * @param roles     Includes the roles the user is allowed to use in the system.
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
public record User(
    String username,
    String firstName,
    String lastName,
    List<String> roles) {
}
