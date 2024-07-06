package org.application.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.application.entity.UserEntity;
import org.application.entity.out.UserDto;
import org.application.entity.in.UserInDto;
import org.application.exception.ResponseException;
import org.application.mapper.UserMapper;
import org.application.repository.UserRepository;
import org.application.repository.RolRepository;
import org.application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RolRepository rolRepository;

    private final UserMapper mapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper mapper, RolRepository rolRepository){
        this.userRepository = userRepository;
        this.rolRepository = rolRepository;
        this.mapper = mapper;
    }


    @Override
    public UserDto findUser(Long id) {
        return Optional.of(id)
                .flatMap(userRepository::findById)
                .map(mapper::map)
                .orElseThrow(() -> new ResponseException(
                        "No se ha encontrado ningun usuario con el id: " +  id, HttpStatus.NOT_FOUND));
    }

    public UserDto createUser(UserInDto userInDto) {
        return Optional.of(userInDto)
                .map(this::validateName)
                .map(mapper::map)
                .map(this::assignRol)
                .map(userRepository::save)
                .map(mapper::map)
                .orElseThrow(() -> new ResponseException(
                        "Bad mapping", HttpStatus.BAD_REQUEST));
    }

    private UserEntity assignRol(UserEntity userEntity){
        return Optional.of("user")
                .flatMap(rolRepository::findByName)
                .map(rol -> {
                    userEntity.addRol(rol);
                    return userEntity;
                })
                .orElseThrow(() -> new ResponseException(
                        "El Rol user no existe y no se puede crear un usuario nuevo.", HttpStatus.INTERNAL_SERVER_ERROR));
    }

    private UserInDto validateName(UserInDto userInDto) {
        return Optional.of(userInDto.getUsername())
                .map(userRepository::findByUsername)
                .filter(Optional::isEmpty)
                .map(given -> userInDto)
                .orElseThrow(() -> new ResponseException(
                        "Usuario con el nombre '" + userInDto.getUsername() + "' ya existe.", HttpStatus.BAD_REQUEST));
    }
}
