package org.application.service;

import org.application.entity.UserEntity;
import org.application.entity.out.UserDto;
import org.application.entity.in.UserInDto;

import java.util.List;
import java.util.Optional;

public interface UserService {

    public UserDto createUser(UserInDto userInDto);

    public Optional<UserDto> findUser(String username, String email);

    public List<UserEntity> findAllUsers();

}
