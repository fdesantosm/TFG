package org.application.service;

import org.application.entity.out.UserDto;
import org.application.entity.in.UserInDto;

public interface UserService {

    public UserDto findUser(Long id);

    public UserDto createUser(UserInDto userInDto);
}
