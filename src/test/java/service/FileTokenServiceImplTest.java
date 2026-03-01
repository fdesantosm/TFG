package service;

import org.application.Main;
import org.application.entity.FileEntity;
import org.application.entity.FileToken;
import org.application.entity.UserEntity;
import org.application.repository.FileRepository;
import org.application.repository.FileTokenRepository;
import org.application.service.impl.FileTokenServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import security.SecurityConfigTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = { Main.class, SecurityConfigTest.class })
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class FileTokenServiceImplTest {


  @Autowired
  JdbcTemplate jdbcTemplate;

  @Mock
  private FileTokenRepository fileTokenRepository;

  @Mock
  private FileRepository fileRepository;

  @InjectMocks
  private FileTokenServiceImpl service;

  private UserEntity owner;

  @AfterAll
  static void cleanDatabase(@Autowired JdbcTemplate jdbcTemplate) {
    jdbcTemplate.execute("DROP TABLE IF EXISTS file_tokens, files, users;");
    jdbcTemplate.execute("DROP SEQUENCE IF EXISTS file_tokens_seq;");
    jdbcTemplate.execute("DROP SEQUENCE IF EXISTS files_seq;");
    jdbcTemplate.execute("DROP SEQUENCE IF EXISTS users_seq;");
    jdbcTemplate.execute("DROP TYPE IF EXISTS user_role;");
  }

  @Test
  void validParameters_whenParametersOK_shouldCreateFileToken() {

    var ownerUser1 = UserEntity.builder()
      .id(87L)
      .username("ownerUser")
      .build();

    var file = FileEntity.builder()
      .id(10L)
      .title("file1")
      .user(ownerUser1)
      .build();

    when(fileRepository.findByTitle("file1"))
      .thenReturn(Optional.of(file));

    when(fileTokenRepository.save(Mockito.<FileToken>any()))
      .thenAnswer(invocation -> invocation.getArgument(0, FileToken.class));

    FileToken token = service.createFileToken("file1", 2, ownerUser1);

    assertNotNull(token);
    assertNotNull(token.getToken());
    assertTrue(token.getExpirationTime().isAfter(LocalDateTime.now()));
    assertEquals(file, token.getFileEntity());
  }

  @Test
  void createToken_whenTimeisNotValid_shouldFail() {
    assertThrows(IllegalArgumentException.class,
      () -> service.createFileToken("file", 0, owner));
  }

  @Test
  void validParameters_whenFileDoesNotExist_returnIllegalArgumentException() {
    when(fileRepository.findByTitle("missing"))
      .thenReturn(Optional.empty());

    assertThrows(IllegalArgumentException.class,
      () -> service.createFileToken("missing", 2, owner));
  }

  @Test
  void validParameters_whenUserDontOwnFile_returnIllegalArgumentException() {

    var otherUser = UserEntity.builder()
      .id(78L)
      .username("otherUser")
      .build();

    var ownerUser = UserEntity.builder()
      .id(87L)
      .username("ownerUser")
      .build();

    var file = FileEntity.builder()
      .title("file2")
      .user(ownerUser)
      .build();

    when(fileRepository.findByTitle("file2"))
      .thenReturn(Optional.of(file));

    assertThrows(IllegalArgumentException.class,
      () -> service.createFileToken("file2", 2, otherUser));
  }
/*
  @Test
  void shouldValidateTokenSuccessfully() {
    FileToken token = FileToken.builder()
      .token("abc")
      .expirationTime(LocalDateTime.now().plusHours(1))
      .build();

    when(fileTokenRepository.findByToken("abc"))
      .thenReturn(Optional.of(token));

    FileToken result = service.validateToken("abc");

    assertEquals(token, result);
  }

  @Test
  void shouldFailWhenTokenIsExpired() {
    FileToken token = FileToken.builder()
      .token("expired")
      .expirationTime(LocalDateTime.now().minusMinutes(1))
      .build();

    when(fileTokenRepository.findByToken("expired"))
      .thenReturn(Optional.of(token));

    assertThrows(IllegalArgumentException.class,
      () -> service.validateToken("expired"));
  }*/
}