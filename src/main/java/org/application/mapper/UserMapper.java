package org.application.mapper;

import org.application.entity.UserEntity;
import org.application.entity.out.UserDto;
import org.application.entity.in.UserInDto;

public interface UserMapper {
    UserEntity map (UserDto userDto);

    UserDto map (UserEntity userEntity);
    UserEntity map (UserInDto userInDto);
}
