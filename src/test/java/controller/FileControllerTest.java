package controller;

import org.application.Main;
import org.application.constant.DirectoryPathConstants;
import org.application.constant.PathConstants;
import org.application.entity.UserEntity;
import org.application.service.FileService;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;
import security.SecurityConfigTest;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


import static org.application.constant.PathConstants.TEST_DOWNLOAD_DIR;
import static org.application.constant.PathConstants.TEST_UPLOAD_DIR;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@SpringBootTest(classes = { Main.class, SecurityConfigTest.class })
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FileControllerTest {

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private FileService fileService;

  @BeforeAll
  static void setupDatabase(@Autowired JdbcTemplate jdbcTemplate) throws Exception {
    String sql = new String(Files.readAllBytes(Paths.get("src/test/resources/sqlScripts/setup_database_file_controller.sql")));
    jdbcTemplate.execute(sql);
    System.out.println("Base de datos configurada correctamente.");
  }

  @BeforeEach
  void setupAuth() throws IOException {
    // Autenticación del usuario
    UserEntity user = new UserEntity();
    user.setId(1L);
    user.setUsername("testUser");
    Authentication auth = new UsernamePasswordAuthenticationToken(user, null, List.of());
    SecurityContextHolder.getContext().setAuthentication(auth);

    //  Crear carpetas temporales para upload y download
    Path tempUpload = Files.createTempDirectory("upload_test_");
    Path tempDownload = Files.createTempDirectory("download_test_");

    DirectoryPathConstants.UPLOAD_DIR = tempUpload.toString() + File.separator;
    DirectoryPathConstants.DOWNLOAD_DIR = tempDownload.toString() + File.separator;

    clearDirectory(DirectoryPathConstants.UPLOAD_DIR);
    clearDirectory(DirectoryPathConstants.DOWNLOAD_DIR);
  }


  private void clearDirectory(String dir) throws IOException {
    Path path = Paths.get(dir);
    if (!Files.exists(path)) {
      Files.createDirectories(path);
      return;
    }
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
      for (Path file : stream) {
        Files.deleteIfExists(file);
      }
    }
  }
  private void prepareDirectories() throws IOException {
    clearDirectory(TEST_UPLOAD_DIR);
    clearDirectory(TEST_DOWNLOAD_DIR);
  }


  @AfterAll
  static void cleanDatabase(@Autowired JdbcTemplate jdbcTemplate) {
    jdbcTemplate.execute("DROP TABLE IF EXISTS file_tokens, files, users;");
    jdbcTemplate.execute("DROP SEQUENCE IF EXISTS file_tokens_seq;");
    jdbcTemplate.execute("DROP SEQUENCE IF EXISTS files_seq;");
    jdbcTemplate.execute("DROP SEQUENCE IF EXISTS users_seq;");
    jdbcTemplate.execute("DROP TYPE IF EXISTS user_role;");
  }


  @Test
  void uploadFile_whenFileIsOkey_shouldReturn201() throws Exception {

    MockMultipartFile sourceFile =
      new MockMultipartFile(
        "file",
        "test.txt",
        "text/plain",
        "archivo de ejemplo".getBytes()
      );

    // Lo envolvemos en un spy para poder mockear transferTo()
    MockMultipartFile spyFile = Mockito.spy(sourceFile);

    // Evitar que file.transferTo intente escribir en el disco
    Mockito.doNothing().when(spyFile).transferTo(Mockito.any(File.class));

    mockMvc.perform(MockMvcRequestBuilders.multipart(PathConstants.TFG + PathConstants.FILE_ROUTE + "/upload")
        .file(spyFile)
        .param("title", "Título del archivo")
        .param("description", "Descripción del archivo"))
      .andExpect(status().isOk())
      .andExpect(content().string("Archivo subido con éxito"));

    verify(fileService).createFileEntity(
      Mockito.eq("Título del archivo"),
      Mockito.anyString(),
      Mockito.eq("Descripción del archivo"),
      Mockito.any()
    );
  }

  @Test
  void uploadFile_whenErrorUploadingFile_shouldReturn500() throws Exception {

    // MockMultipartFile real
    MockMultipartFile mockMvcFile =
      new MockMultipartFile("file", "test.txt", "text/plain", "dummy".getBytes());

    // Spy del archivo para mockear transferTo()
    MultipartFile spyFile = Mockito.spy(mockMvcFile);

    // Hacemos que falle transferTo()
    Mockito.doThrow(new IOException("Simulated IO failure"))
      .when(spyFile)
      .transferTo(Mockito.any(File.class));

    mockMvc.perform(MockMvcRequestBuilders.multipart(PathConstants.TFG + PathConstants.FILE_ROUTE + "/upload")
        .file((MockMultipartFile) spyFile)
        .param("title", "titulo")
        .param("description", "desc")
        .with(request -> {
          UserEntity user = new UserEntity();
          user.setId(1L);
          user.setUsername("adminUser");
          SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken(
              user, null, List.of(new SimpleGrantedAuthority("ADMIN"))
            )
          );
          return request;
        })
      )
      .andExpect(status().isInternalServerError())
      .andExpect(content().string("Error while uploading file"));
  }

  @Test
  void downloadFile_whenFileExists_shouldReturn200() throws Exception {
    String token = "testfile.txt";

    // Crear archivo de prueba en UPLOAD_DIR
    Path uploadFile = Paths.get(DirectoryPathConstants.UPLOAD_DIR + token);
    Files.createDirectories(uploadFile.getParent());
    Files.writeString(uploadFile, "contenido test");

    assertTrue(Files.exists(uploadFile), "El archivo de prueba no se creó correctamente");

    // MockMvc con usuario ADMIN
    mockMvc.perform(get(PathConstants.TFG + PathConstants.FILE_ROUTE + "/download/" + token)
        .with(SecurityMockMvcRequestPostProcessors.user("adminUser")
          .authorities(new SimpleGrantedAuthority("ADMIN"))))
      .andExpect(status().isOk())
      .andExpect(content().string("Se ha completado la descarga con éxito"));

    Path downloadFile = Paths.get(DirectoryPathConstants.DOWNLOAD_DIR + token);
    assertTrue(Files.exists(downloadFile), "El archivo no se copió correctamente");
  }

  @Test
  void downloadFile_whenFileDoesntExists_shouldReturn404() throws Exception {
    String token = "testfile.txt";

    // MockMvc con usuario ADMIN
    mockMvc.perform(get(PathConstants.TFG + PathConstants.FILE_ROUTE + "/download/" + token)
        .with(SecurityMockMvcRequestPostProcessors.user("adminUser")
          .authorities(new SimpleGrantedAuthority("ADMIN"))))
      .andExpect(status().isNotFound());

  }

  @Test
  void findPublicFilesFromUser_whenCorrectUsernameAndPublicFilesExists_shouldReturn200() throws Exception {
    String username = "username4";

    mockMvc.perform(get(PathConstants.TFG + PathConstants.FILE_ROUTE + "/publicFiles/" + username)
        .with(SecurityMockMvcRequestPostProcessors.user("adminUser")
          .authorities(new SimpleGrantedAuthority("ADMIN")))
      )
      .andExpect(status().isOk());

  }

  @Test
  void findPublicFilesFromUser_whenUserDoesntHavePublicFiles_shouldReturn404() throws Exception {

    String username = "username5";

    when(fileService.findPublicFilesFromUser(username))
      .thenThrow(new RuntimeException("Archivos públicos no encontrados para el usuario: " + username));

    mockMvc.perform(get(PathConstants.TFG + PathConstants.FILE_ROUTE + "/publicFiles/" + username))
      .andExpect(status().isNotFound());
  }

}
