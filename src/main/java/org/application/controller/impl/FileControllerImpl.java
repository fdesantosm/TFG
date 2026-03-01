package org.application.controller.impl;

import org.application.constant.DirectoryPathConstants;
import org.application.controller.FileController;
import org.application.entity.UserEntity;
import org.application.entity.out.FileDto;
import org.application.service.FileService;
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
import java.util.List;
import java.util.UUID;

@RestController
public class FileControllerImpl implements FileController {
    private FileService fileService;

    @Autowired
    public FileControllerImpl( FileService fileService) {
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

        }
    }

    public ResponseEntity<String> downloadFile(String token) throws IOException {

        String uploadFilePath = DirectoryPathConstants.UPLOAD_DIR + token;
        File file = new File(uploadFilePath);

        if (!file.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Archivo no encontrado");
        }

        String downloadFilePath = DirectoryPathConstants.DOWNLOAD_DIR + token;
        File downloadFile = new File(downloadFilePath);

        Files. copy(file.toPath(), downloadFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

        return ResponseEntity.ok("Se ha completado la descarga con éxito");
    }

    public ResponseEntity<List<FileDto>> findPublicFilesFromUser(String username){
        try {
            List<FileDto> files = fileService.findPublicFilesFromUser(username);
            return ResponseEntity.ok(files);
        } catch (RuntimeException e) {
            return ResponseEntity
              .status(HttpStatus.NOT_FOUND)
              .build();
        }
    }
}
