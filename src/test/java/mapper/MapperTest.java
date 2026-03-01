package mapper;

import org.application.Main;
import org.application.entity.UserEntity;
import org.application.entity.enums.RoleConstants;
import org.application.entity.in.UserInDto;
import org.application.mapper.impl.UserMapperImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;


@SpringBootTest(classes = Main.class)
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MapperTest {

  @Mock
  private PasswordEncoder passwordEncoder;

  @InjectMocks
  private UserMapperImpl mapper;

  @Test
  void mapUserInDtoToEntity_shouldMapOk() {
    UserInDto dto = new UserInDto();
    dto.setUsername("user1");
    dto.setEmail("user1@mail.com");
    dto.setPassword("12345");

    when(passwordEncoder.encode("12345"))
      .thenReturn("encryptedPassword");

    UserEntity entity = mapper.map(dto);

    assertNotNull(entity);
    assertEquals("user1", entity.getUsername());
    assertEquals("user1@mail.com", entity.getEmail());
    assertEquals("encryptedPassword", entity.getPassword());
    assertEquals(RoleConstants.USER, entity.getRole());
  }

}



