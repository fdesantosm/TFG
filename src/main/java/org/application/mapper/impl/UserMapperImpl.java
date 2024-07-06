package org.application.mapper.impl;

import org.application.entity.UserEntity;
import org.application.entity.Rol;
import org.application.entity.out.UserDto;
import org.application.entity.in.UserInDto;
import org.application.mapper.UserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

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
        userDto.setRoles(mapRolesToString(userEntity.getRoles()));

        return userDto;
    }

    public List<String> mapRolesToString(List<Rol> rols){
        return rols.stream()
                .map(Rol::getName)
                .toList();
    }

    public UserEntity map(UserInDto userInDto){
        if (userInDto == null){
            return null;
        }
        return UserEntity.builder()
                .username(userInDto.getUsername())
                .email(userInDto.getEmail())
                .password(passwordEncoder.encode(userInDto.getPassword()))
                .roles(new ArrayList<>())
                .build();

    }
}
