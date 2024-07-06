package org.application.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PathConstants {

    public static final String AT = "/at";
    public static final String USER_ROUTE = "/user";
    public static final String VERIFICATION_ROUTE = "/verification";

    /**
     * Routes for security configuration
     */
    public static final String REGISTER_ROUTE = "/at/verification/register/**";
    public static final String LOGIN_ROUTE = "/at/verification/login/**";
    public static final String PRUEBA_ROUTE = "/at/user/prueba/**";

}
