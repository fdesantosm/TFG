package org.application.controller.impl;

import org.application.constant.PathConstants;
import org.application.controller.VerificationController;
import org.application.entity.in.LoginDto;
import org.application.entity.out.AuthDto;
import org.application.entity.out.UserDto;
import org.application.entity.in.UserInDto;
import org.application.security.JwtGenerator;
import org.application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class VerificationControllerImpl implements VerificationController {

    private AuthenticationManager authenticationManager;
    private UserService userService;
    private JwtGenerator jwtGenerator;

    @Autowired
    public VerificationControllerImpl(AuthenticationManager authenticationManager, UserService userService, JwtGenerator jwtGenerator) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtGenerator =jwtGenerator;
    }

    public ResponseEntity<UserDto> register (UserInDto userInDto){
        var savedUser = userService.createUser(userInDto);
        return ResponseEntity.created(URI.create(PathConstants.USER_ROUTE))
                .contentType(MediaType.APPLICATION_JSON).body(savedUser);
    }

    public ResponseEntity<AuthDto> login(LoginDto loginDto){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);
        return new ResponseEntity<>(new AuthDto(token), HttpStatus.OK);
   }
}
