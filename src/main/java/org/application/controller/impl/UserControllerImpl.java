package org.application.controller.impl;

import org.application.constant.PathConstants;
import org.application.controller.UserControllerApi;
import org.application.entity.out.UserDto;
import org.application.entity.in.UserInDto;
import org.application.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class UserControllerImpl implements UserControllerApi {
    private final UserService userService;

    public UserControllerImpl(UserService userService){
        this.userService = userService;
    }

    public ResponseEntity<String> testDePrueba(){
        return new ResponseEntity<>("Hola Mundo", HttpStatus.OK);
    }


    @Override
    public ResponseEntity<UserDto> findUser(Long id){
        return new ResponseEntity<>(userService.findUser(id), HttpStatus.OK);
    }

    public ResponseEntity<UserDto> createUser(UserInDto userInDto){
        var savedUser = userService.createUser(userInDto);
        return ResponseEntity.created(URI.create(PathConstants.AT + PathConstants.USER_ROUTE))
                .contentType(MediaType.APPLICATION_JSON).body(savedUser);
    }
}
