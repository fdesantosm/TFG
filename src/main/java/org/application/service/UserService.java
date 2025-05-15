package org.application.service;

import org.application.entity.UserEntity;
import org.application.entity.out.UserDto;
import org.application.entity.in.UserInDto;

import java.util.List;

public interface UserService {

    public List<UserEntity> findAllUsers();

    public UserDto findUser(Long id);

    public UserDto createUser(UserInDto userInDto);
}
