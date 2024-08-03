package org.application.controller.impl;

import org.application.constant.DirectoryPathConstants;
import org.application.controller.FileController;
import org.application.entity.UserEntity;
import org.application.service.FileService;
import org.application.service.FileTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@RestController
public class FileControllerImpl implements FileController {
    private FileTokenService fileTokenService;
    private FileService fileService;

    @Autowired
    public FileControllerImpl(FileTokenService fileTokenService, FileService fileService) {
        this.fileTokenService = fileTokenService;
        this.fileService = fileService;
    }

    public ResponseEntity<String> uploadFile(MultipartFile file, String title, String description) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserEntity user = (UserEntity) authentication.getPrincipal();
            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
            String fileName = UUID.randomUUID().toString() + "_" + originalFilename;
            File uploadFile = new File(DirectoryPathConstants.UPLOAD_DIR + fileName);

            file.transferTo(uploadFile);
            fileService.createFileEntity(title, fileName, description, user);

            return new ResponseEntity<>("Archivo subido con éxito", HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while uploading file");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error occurred");
        }
    }


    @GetMapping("/download/{token}")
    public ResponseEntity<String> downloadFile(String token) {
        try {

            String uploadFilePath = DirectoryPathConstants.UPLOAD_DIR + token;
            File file = new File(uploadFilePath);

            if (!file.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            String downloadFilePath = DirectoryPathConstants.DOWNLOAD_DIR + token;
            File downloadFile = new File(downloadFilePath);

            // Copiar el archivo de carga al archivo de descarga
            Files.copy(file.toPath(), downloadFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            return ResponseEntity.ok("Se ha completado la descarga con éxito");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
