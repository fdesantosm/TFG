package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.application.Main;
import org.application.constant.PathConstants;
import org.application.entity.in.UserInDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.hamcrest.Matchers.*;

import java.nio.file.Files;
import java.nio.file.Paths;


import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(classes = Main.class)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserControllerTest {

  @Autowired
  private WebApplicationContext webApplicationContext;

  private MockMvc mockMvc;

  @BeforeAll
  static void setupDatabase(@Autowired JdbcTemplate jdbcTemplate) throws Exception {
    String sql = new String(Files.readAllBytes(Paths.get("src/test/resources/sqlScripts/setup_database_user_controller.sql")));
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
  void createUser_whenDataIsOk_shouldReturn201() throws Exception {

    var username = "user1";
    var email = "correodeshechable@ejemplo.com";

    var userInDto = UserInDto.builder()
      .username(username)
      .password("asdf1234ASDF!")
      .email(email)
      .build();

    mockMvc.perform(
        post(PathConstants.TFG + PathConstants.USER_ROUTE)
          .content(asJson(userInDto))
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated())
      .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
      .andReturn();


    mockMvc.perform(
        get(PathConstants.TFG + PathConstants.USER_ROUTE)
          .param("username", "user1")
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.username").value("user1"))
      .andExpect(jsonPath("$.email").value("correodeshechable@ejemplo.com"))
      .andReturn();
  }

  @Test
  void createUser_whenUsernameAlreadyExists_shouldReturn409() throws Exception {

    var userInDto2 = UserInDto.builder()
      .username("user2")
      .password("asdf1234ASDF!")
      .email("correo2@ejemplo.com")
      .build();

    var userInDto3 = UserInDto.builder()
      .username("user2")
      .password("asdf1234ASDF!")
      .email("correo2@ejemplo.com")
      .build();

    mockMvc.perform(
        post(PathConstants.TFG + PathConstants.USER_ROUTE)
          .content(asJson(userInDto2))
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated())
      .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
      .andReturn();

    mockMvc.perform(
        post(PathConstants.TFG + PathConstants.USER_ROUTE)
          .content(asJson(userInDto3))
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isConflict())
      .andReturn();
  }

  @Test
  void createUser_whenEmailAlreadyExists_shouldReturn409() throws Exception {

    var userInDto4 = UserInDto.builder()
      .username("user4")
      .password("asdf1234ASDF!")
      .email("correo4@ejemplo.com")
      .build();

    var userInDto5 = UserInDto.builder()
      .username("user5")
      .password("asdf1234ASDF!")
      .email("correo4@ejemplo.com")
      .build();

    mockMvc.perform(
        post(PathConstants.TFG + PathConstants.USER_ROUTE)
          .content(asJson(userInDto4))
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated())
      .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
      .andReturn();

    mockMvc.perform(
        post(PathConstants.TFG + PathConstants.USER_ROUTE)
          .content(asJson(userInDto5))
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isConflict())
      .andReturn();
  }


  @Test
  void findAllUser_whenUsersExists_shouldReturn200() throws Exception {

    var userInDto6 = UserInDto.builder()
      .username("user6")
      .password("asdf1234ASDF!")
      .email("correo6@ejemplo.com")
      .build();

    mockMvc.perform(
        post(PathConstants.TFG + PathConstants.USER_ROUTE)
          .content(asJson(userInDto6))
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated())
      .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
      .andReturn();

    mockMvc.perform(
        get(PathConstants.TFG + PathConstants.USER_ROUTE + "/all")
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$", hasSize(greaterThan(0))))
      .andReturn();
  }

  @Test
  void findUser_whenUsersWithUsernameAndEmailExists_shouldReturn200() throws Exception {

    var userInDto7 = UserInDto.builder()
      .username("user7")
      .password("asdf1234ASDF!")
      .email("correo7@ejemplo.com")
      .build();

    mockMvc.perform(
        post(PathConstants.TFG + PathConstants.USER_ROUTE)
          .content(asJson(userInDto7))
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated())
      .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
      .andReturn();

    mockMvc.perform(
        get(PathConstants.TFG + PathConstants.USER_ROUTE)
          .param("username", "user7")
          .param("email", "correo7@ejemplo.com")
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.username").value("user7"))
      .andExpect(jsonPath("$.email").value("correo7@ejemplo.com"));
  }

  @Test
  void findUser_whenUsersWithUsernameAndEmailNotExists_shouldReturn404() throws Exception {

    var userInDto7 = UserInDto.builder()
      .username("user8")
      .password("asdf1234ASDF!")
      .email("correo8@ejemplo.com")
      .build();

    mockMvc.perform(
        post(PathConstants.TFG + PathConstants.USER_ROUTE)
          .content(asJson(userInDto7))
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated())
      .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
      .andReturn();

    mockMvc.perform(
        get(PathConstants.TFG + PathConstants.USER_ROUTE)
          .param("username", "usuarioInexistente")
          .param("email", "correoinexistente@inventada.com")
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isNotFound());
  }

  @Test
  void findUser_whenUsersAndUsernameNotIncluded_shouldReturn400() throws Exception {

    var userInDto7 = UserInDto.builder()
      .username("user9")
      .password("asdf1234ASDF!")
      .email("correo9@ejemplo.com")
      .build();

    mockMvc.perform(
        post(PathConstants.TFG + PathConstants.USER_ROUTE)
          .content(asJson(userInDto7))
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated())
      .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
      .andReturn();

    mockMvc.perform(
        get(PathConstants.TFG + PathConstants.USER_ROUTE)
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest());
  }


  private static String asJson(Object o){
    try{
      return new ObjectMapper().writeValueAsString(o);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}



