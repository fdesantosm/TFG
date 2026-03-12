package service;

import org.application.entity.UserEntity;
import org.application.entity.in.PasswordDto;
import org.application.entity.in.UserInDto;
import org.application.entity.out.UserDto;
import org.application.exception.ResponseException;
import org.application.mapper.UserMapper;
import org.application.repository.UserRepository;
import org.application.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserMapper mapper;

  @Mock
  private PasswordEncoder passwordEncoder;

  @InjectMocks
  private UserServiceImpl userService;

  UserEntity user1 = UserEntity.builder()
    .id(1L)
    .username("pepe")
    .email("pepe@mail.com")
    .build();

  private UserDto userDto;
  private UserInDto userInDto;

  @BeforeEach
  void setup() {

    userDto = UserDto.builder()
      .id(1L)
      .username("pepe")
      .email("pepe@mail.com")
      .build();

    userInDto = new UserInDto();
    userInDto.setUsername("pepe");
    userInDto.setEmail("pepe@mail.com");
  }

  @Test
  void findAllUsers_whenExistsUsers_returnList() {

    when(userRepository.findAll()).thenReturn(List.of(user1));

    List<UserDto> result = userService.findAllUsers();

    assertEquals(1, result.size());
    verify(userRepository).findAll();
  }

  @Test
  void findAllUsers_whenNotExistsUser_returnEmptyList() {

    when(userRepository.findAll()).thenReturn(List.of());

    List<UserDto> result = userService.findAllUsers();

    assertEquals(0, result.size());
    verify(userRepository).findAll();
  }

  @Test
  void findUser_whenUserExists_returnUser() {

    when(userRepository.findByUsernameOrEmail("pepe", "pepe@email.com"))
      .thenReturn(Optional.of(user1));

    Optional<UserDto> result = userService.findUser("pepe", "pepe@email.com");

    assertTrue(result.isPresent());
    assertEquals("pepe", result.get().getUsername());

    verify(userRepository).findByUsernameOrEmail("pepe", "pepe@email.com");
  }


  @Test
  void findUser_whenUserNotExists_returnNotFound() {

    when(userRepository.findByUsernameOrEmail("wrongName", "wrong@mail.com"))
      .thenReturn(Optional.empty());

    Optional<UserDto> result = userService.findUser("wrongName", "wrong@mail.com");

    assertTrue(result.isEmpty());
  }


  @Test
  void createUser_whenInputsOk_returnCreatedUser() {

    when(userRepository.findByUsername("pepe"))
      .thenReturn(Optional.empty());
    when(userRepository.findByEmail("pepe@mail.com"))
      .thenReturn(Optional.empty());
    when(mapper.map(userInDto)).thenReturn(user1);
    when(userRepository.save(user1)).thenReturn(user1);
    when(mapper.map(user1)).thenReturn(userDto);

    UserDto result = userService.createUser(userInDto);

    assertNotNull(result);
    assertEquals("pepe", result.getUsername());

    verify(userRepository).save(user1);
  }


  @Test
  void createUser_whenUsernameExists_returnException() {

    when(userRepository.findByUsername("pepe"))
      .thenReturn(Optional.of(user1));

    ResponseException ex = assertThrows(ResponseException.class,
      () -> userService.createUser(userInDto));

    assertEquals(HttpStatus.CONFLICT, ex.getStatus());

    verify(userRepository, never()).save(any());
  }

  @Test
  void createUser_whenEmailExists_returnThrowException() {

    when(userRepository.findByUsername("pepe"))
      .thenReturn(Optional.empty());

    when(userRepository.findByEmail("pepe@mail.com"))
      .thenReturn(Optional.of(user1));

    ResponseException ex = assertThrows(ResponseException.class,
      () -> userService.createUser(userInDto));

    assertEquals(HttpStatus.CONFLICT, ex.getStatus());

    verify(userRepository, never()).save(any());
  }

  @Test
  void changePassword_whenNewPasswordOk_changePasswordSuccessfully() {

    UserEntity user = UserEntity.builder()
      .id(7L)
      .username("user7")
      .password("encodedOldPassword")
      .build();

    PasswordDto dto = new PasswordDto("oldPassword", "newPassword");

    when(userRepository.findByUsername("user7")).thenReturn(Optional.of(user));
    when(passwordEncoder.matches("oldPassword", "encodedOldPassword")).thenReturn(true);
    when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");

    userService.changePassword("user7", dto);

    verify(userRepository).save(user);
    assertEquals("encodedNewPassword", user.getPassword());
  }

  @Test
  void changePassword_whenUserNotFound_throw404() {

    PasswordDto dto = new PasswordDto("oldPassword", "newPassword");

    when(userRepository.findByUsername("user8")).thenReturn(Optional.empty());

    ResponseException ex = assertThrows(
      ResponseException.class, () -> userService.changePassword("user8", dto)
    );

    assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());

    verify(userRepository, never()).save(any());
  }

  @Test
  void changePassword_whenCurrentPasswordWrong_throwException() {

    UserEntity user = UserEntity.builder()
      .id(9L)
      .username("user9")
      .password("encodedOldPassword")
      .build();

    PasswordDto dto = new PasswordDto("wrongPassword", "newPassword");

    when(userRepository.findByUsername("user9")).thenReturn(Optional.of(user));
    when(passwordEncoder.matches("wrongPassword", "encodedOldPassword")).thenReturn(false);

    ResponseException ex = assertThrows(
      ResponseException.class,
      () -> userService.changePassword("user9", dto)
    );

    assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());

    verify(userRepository, never()).save(any());
  }



  /// /////Revisar test y hacer los siquientes casos/////
}