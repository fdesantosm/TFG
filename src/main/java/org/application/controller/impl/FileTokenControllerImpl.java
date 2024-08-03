package org.application.controller.impl;

import org.application.controller.FileTokenController;
import org.application.entity.UserEntity;
import org.application.service.impl.FileTokenServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FileTokenControllerImpl implements FileTokenController {
    private FileTokenServiceImpl fileTokenService;

    public FileTokenControllerImpl(FileTokenServiceImpl fileTokenService) {
        this.fileTokenService = fileTokenService;
    }

    @Override
    public ResponseEntity<String> createPrivateToken(String title, long duration) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity authentedUser = (UserEntity) authentication.getPrincipal();
        var token = fileTokenService.createFileToken(title, duration, authentedUser);
        return new ResponseEntity<>("Token creado con exito: " + token.getToken(), HttpStatus.OK);
    }
}
