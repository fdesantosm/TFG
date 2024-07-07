package org.application.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityConstants {
    public static final long JWT_EXPIRATION = 120000;
    public static final String JWT_SECRET = "9da71dfa09b85ec829fdaff5fb8e342e88abc71ac668597c3be98a7ff6e3457a";
}
