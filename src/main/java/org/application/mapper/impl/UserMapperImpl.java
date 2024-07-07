package org.application.mapper.impl;

import org.application.constant.RoleConstants;
import org.application.entity.UserEntity;
import org.application.entity.out.UserDto;
import org.application.entity.in.UserInDto;
import org.application.mapper.UserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements UserMapper {
    private PasswordEncoder passwordEncoder;

    public UserMapperImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public UserEntity map(UserDto userDto){
        if (userDto == null){
            return null;
        }
        var userEntity = new UserEntity();
        BeanUtils.copyProperties(userDto, userEntity);

        return userEntity;
    }

    public UserDto map(UserEntity userEntity){
        if (userEntity == null){
            return null;
        }
        var userDto = new UserDto();
        BeanUtils.copyProperties(userEntity, userDto);

        return userDto;
    }

    public UserEntity map(UserInDto userInDto){
        if (userInDto == null){
            return null;
        }
        return UserEntity.builder()
                .username(userInDto.getUsername())
                .email(userInDto.getEmail())
                .password(passwordEncoder.encode(userInDto.getPassword()))
                .role(RoleConstants.USER)
                .build();

    }
}
