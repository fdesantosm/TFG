package service;

import org.application.entity.FileEntity;
import org.application.entity.UserEntity;
import org.application.entity.out.FileDto;
import org.application.repository.FileRepository;
import org.application.repository.UserRepository;
import org.application.service.impl.FileServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileServiceImplTest {

  @Mock
  private FileRepository fileRepository;

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private FileServiceImpl fileService;

  @Test
  void createFileEntity_whenInputsAreOk_shouldCreateAndSaveFile() {

    String title = "Archivo";
    String fileName = "uuid_document.pdf";
    String description = "Descripción del archivo";

    UserEntity user = UserEntity.builder()
      .id(1L)
      .username("user1")
      .email("user1@mail.com")
      .build();

    when(fileRepository.save(any(FileEntity.class)))
      .thenAnswer(invocation -> invocation.getArgument(0));

    FileEntity result = fileService.createFileEntity(
      title, fileName, description, user
    );

    assertNotNull(result);
    assertEquals(title, result.getTitle());
    assertEquals(fileName, result.getFileName());
    assertEquals(description, result.getDescription());
    assertFalse(result.isVisible());
    assertEquals(user, result.getUser());

    verify(fileRepository, times(1)).save(any(FileEntity.class));
  }

  @Test
  void findPublicFilesFromUsername_whenUserExistsAndHasFiles_returnPublicFiles() {

    String username = "username1";
    Long userId = 1L;

    FileDto file = new FileDto(userId, "titulo", "description", "publicToken");

    when(userRepository.findPublicUserByUsername(username))
      .thenReturn(Optional.of(userId));

    when(fileRepository.findPublicFilesByUsername(userId))
      .thenReturn(List.of(file));

    List<FileDto> result = fileService.findPublicFilesFromUser(username);

    assertEquals(1, result.size());
    assertEquals("titulo", result.get(0).getTitle());
    assertEquals("description", result.get(0).getDescription());
    assertEquals("publicToken", result.get(0).getPublicToken());
  }


  @Test
  void findPublicFilesByUsername_whenUserDoesNotExists_returnRunTimeException() {

    when(userRepository.findPublicUserByUsername("username2"))
      .thenReturn(Optional.empty());

    RuntimeException ex = assertThrows(RuntimeException.class,
      () -> fileService.findPublicFilesFromUser("username2"));

    assertEquals("Usuario no encontrado", ex.getMessage());

  }

  @Test
  void findPublicFilesByUsername_whenPublicFilesNotFound_returnRunTimeException() {

    String username = "username3";
    Long userId = 2L;

    when(userRepository.findPublicUserByUsername(username))
      .thenReturn(Optional.of(userId));
    when(fileRepository.findPublicFilesByUsername(userId))
      .thenReturn(List.of());

    RuntimeException ex = assertThrows(RuntimeException.class,
      () -> fileService.findPublicFilesFromUser("username3"));
    assertEquals("No se encontraron archivos públicos para el usuario", ex.getMessage());

  }
}