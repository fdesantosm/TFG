package org.application.service.impl;

import lombok.extern.slf4j.Slf4j;

import org.application.entity.UserEntity;
import org.application.entity.out.UserDto;
import org.application.entity.in.UserInDto;
import org.application.exception.ResponseException;
import org.application.mapper.UserMapper;
import org.application.repository.UserRepository;
import org.application.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  private final UserMapper mapper;


  @Autowired
  public UserServiceImpl(UserRepository userRepository, UserMapper mapper) {
    this.userRepository = userRepository;
    this.mapper = mapper;
  }

  public UserDto createUser(UserInDto userInDto) {
    return Optional.of(userInDto)
      .map(this::validateName)
      .map(this::validateEmail)
      .map(mapper::map)
      .map(userRepository::save)
      .map(mapper::map)
      .orElseThrow(() -> new ResponseException(
        "Bad mapping", HttpStatus.BAD_REQUEST));
  }

  @Override
  public Optional<UserDto> findUser(String username, String email) {
    return userRepository
      .findByUsernameOrEmail(username, email)
      .map(user -> UserDto.builder()
        .id(user.getId())
        .username(user.getUsername())
        .email(user.getEmail())
        .role(user.getRole())
        .build());
  }

  @Override
  public List<UserEntity> findAllUsers() {
   return userRepository.findAll();
  }

  private UserInDto validateName(UserInDto userInDto) {
    return Optional.of(userInDto.getUsername())
        .map(userRepository::findByUsername)
        .filter(Optional::isEmpty)
        .map(given -> userInDto)
        .orElseThrow(() -> new ResponseException(
            "Usuario con el nombre '" + userInDto.getUsername() + "' ya existe.", HttpStatus.CONFLICT));
  }

  private UserInDto validateEmail(UserInDto userInDto) {
    return Optional.of(userInDto.getEmail())
      .map(userRepository::findByEmail)
      .filter(Optional::isEmpty)
      .map(given -> userInDto)
      .orElseThrow(() -> new ResponseException(
        "Usuario con el email '" + userInDto.getEmail() + "' ya existe.", HttpStatus.CONFLICT));
  }
}
