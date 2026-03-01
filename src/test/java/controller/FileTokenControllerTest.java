package controller;

import org.application.Main;
import org.application.constant.PathConstants;
import org.application.entity.FileToken;
import org.application.entity.UserEntity;
import org.application.repository.FileRepository;
import org.application.repository.FileTokenRepository;
import org.application.service.impl.FileTokenServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import security.SecurityConfigTest;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest(classes = { Main.class, SecurityConfigTest.class })
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FileTokenControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private FileTokenServiceImpl fileTokenService;

  @MockBean
  private FileRepository fileRepository;

  @MockBean
  private FileTokenRepository fileTokenRepository;

  private UserEntity ownerUser;
  private UserEntity otherUser;

  @BeforeEach
  void setup() {
    ownerUser = new UserEntity();
    ownerUser.setId(10L);
    ownerUser.setUsername("ownerUser");

    otherUser = new UserEntity();
    otherUser.setId(99L);
    otherUser.setUsername("otherUser");
  }

  private RequestPostProcessor authorization(UserEntity user) {
    return request -> {
      UsernamePasswordAuthenticationToken auth =
        new UsernamePasswordAuthenticationToken(user, null,
          List.of(new SimpleGrantedAuthority("USER")));
      SecurityContextHolder.getContext().setAuthentication(auth);
      return request;
    };
  }

  @Test
  void createPrivateToken_whenUserIsAuthorized_shouldReturn200() throws Exception {

    UserEntity user = new UserEntity();
    user.setId(1L);
    user.setUsername("user1");

    Mockito.when(fileTokenService.createFileToken(
      Mockito.anyString(),
      Mockito.anyLong(),
      Mockito.any(UserEntity.class)
    )).thenReturn(
      FileToken.builder().token("abc123").build()
    );

    mockMvc.perform(post(PathConstants.TFG + PathConstants.FILE_TOKEN_ROUTE)
        .param("title", "archivo1")
        .param("duration", "2")
        .with(authentication(
          new UsernamePasswordAuthenticationToken(
            user,null, List.of(new SimpleGrantedAuthority("ROLE_USER"))
          )
        ))
    )
    .andExpect(status().isOk())
    .andExpect(content().string(containsString("Token creado con exito")));
  }

  @Test
  void createPrivateToken_whenDurationInvalid_shouldReturn400() throws Exception {

    UserEntity user = new UserEntity();
    user.setId(1L);
    user.setUsername("user1");

    // El service lanza IllegalArgumentException
    Mockito.when(fileTokenService.createFileToken(
      Mockito.eq("archivo1"),
      Mockito.eq(0L),
      Mockito.any(UserEntity.class)
    )).thenThrow(new IllegalArgumentException("La duración debe ser al menos de una hora."));

    mockMvc.perform(post(PathConstants.TFG + PathConstants.FILE_TOKEN_ROUTE)
        .param("title", "archivo1")
        .param("duration", "0")
        .with(authentication(
          new UsernamePasswordAuthenticationToken(
            user,
            null,
            List.of(new SimpleGrantedAuthority("ROLE_USER"))
          )
        ))
      )
      .andExpect(status().isBadRequest())
      .andExpect(content().string(containsString("La duración debe ser al menos de una hora.")))
    ;
  }


  @Test
  void createPrivateToken_whenFileNotFound_shouldReturn400() throws Exception {

    UserEntity user = new UserEntity();
    user.setId(1L);

    Mockito.when(fileTokenService.createFileToken(
      Mockito.eq("archivoInexistente"),
      Mockito.eq(2L),
      Mockito.any(UserEntity.class)
    )).thenThrow(new IllegalArgumentException("Archivo con el título archivoInexistente no encontrado."));

    mockMvc.perform(post(PathConstants.TFG + PathConstants.FILE_TOKEN_ROUTE)
      .param("title", "archivoInexistente")
      .param("duration", "2")
      .with(authentication( new UsernamePasswordAuthenticationToken( user,null,
          List.of(new SimpleGrantedAuthority("ROLE_USER"))
        )
      ))
    )
    .andExpect(status().isBadRequest())
    .andExpect(content().string(containsString("Archivo con el título archivoInexistente no encontrado.")));
  }
  
}
