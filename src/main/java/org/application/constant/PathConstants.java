package org.application.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PathConstants {

    public static final String TFG = "/tfg";
    public static final String USER_ROUTE = "/user";
    public static final String VERIFICATION_ROUTE = "/verification";
    public static final String FILE_ROUTE = "/file";
    public static final String FILE_TOKEN_ROUTE = "/filetoken";

    /**
     * Routes for security configuration
     */
    public static final String REGISTER_ROUTE = "/tfg/verification/register/**";
    public static final String LOGIN_ROUTE = "/tfg/verification/login/**";
    public static final String PRUEBA_ROUTE = "/tfg/user/prueba/**";
    public static final String DOWNLOAD_ROUTE = "/tfg/file/download/**";

    /**
     * Routes for tests
     */
    public static final String TEST_UPLOAD_DIR =
      System.getProperty("java.io.tmpdir") + "/tfg_test_upload/";
    public static final String TEST_DOWNLOAD_DIR =
      System.getProperty("java.io.tmpdir") + "/tfg_test_download/";

}
