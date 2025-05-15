package controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.application.Main;
import org.application.constant.PathConstants;
import org.application.controller.impl.VerificationControllerImpl;
import org.application.entity.in.LoginDto;
import org.application.entity.in.UserInDto;
import org.application.security.JwtGenerator;
import org.application.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Main.class)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class VerificationControllerTest{

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtGenerator jwtGenerator;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    VerificationControllerImpl verificationController;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeAll
    static void setupDatabase(@Autowired JdbcTemplate jdbcTemplate) throws Exception {
        String sql = new String(Files.readAllBytes(Paths.get("src/test/resources/sqlScripts/setup_database.sql")));
        jdbcTemplate.execute(sql);
        System.out.println("Base de datos configurada correctamente.");
    }

    @AfterAll
    static void cleanDatabase(@Autowired JdbcTemplate jdbcTemplate) {
        jdbcTemplate.execute("DROP TABLE IF EXISTS file_tokens, files, users;");
        jdbcTemplate.execute("DROP SEQUENCE IF EXISTS file_tokens_seq;");
        jdbcTemplate.execute("DROP SEQUENCE IF EXISTS files_seq;");
        jdbcTemplate.execute("DROP SEQUENCE IF EXISTS users_seq;");
        jdbcTemplate.execute("DROP TYPE IF EXISTS user_role;");
    }

    @BeforeEach
    void setupMockMvc() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }
    @Test
    void register_whenPositiveRegister_createUser() throws Exception{
        var userInDto = UserInDto.builder()
                .username("Asteroide de kebabs")
                .password("asiasiasiganaelMadrid")
                .email("mrtartariah@algo.com")
                .build();

        var loginDto = new LoginDto();
        loginDto.setUsername("testUser1");
        loginDto.setPassword("testPassword1");

        mockMvc.perform(
                        post(PathConstants.TFG + PathConstants.VERIFICATION_ROUTE + "/register")
                                .content(asJson(userInDto))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }


    private static String asJson(Object o){
        try{
            return new ObjectMapper().writeValueAsString(o);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
