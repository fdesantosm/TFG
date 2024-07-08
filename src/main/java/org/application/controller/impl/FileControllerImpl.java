package org.application.controller.impl;

import org.application.constant.DirectoryPathConstants;
import org.application.controller.FileController;
import org.application.entity.FileToken;
import org.application.service.FileTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    public FileControllerImpl(FileTokenService fileTokenService) {
        this.fileTokenService = fileTokenService;
    }

    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,
                                             @RequestParam("tokenDuration") long tokenDuration) {
        try {
            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
            String fileIdentifier = UUID.randomUUID().toString() + "_" + originalFilename;

            File uploadFile = new File(DirectoryPathConstants.UPLOAD_DIR + fileIdentifier);
            file.transferTo(uploadFile);

            // Generate a token for file download with expiration time
            FileToken fileToken = fileTokenService.createFileToken(fileIdentifier, tokenDuration);

            return ResponseEntity.ok(fileToken.getToken());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while uploading file");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error occurred");
        }
    }


    @GetMapping("/download/{token}")
    public ResponseEntity<String> downloadFile(@PathVariable String token) {
        try {
            FileToken fileToken = fileTokenService.validateToken(token);
            String fileIdentifier = fileToken.getFileIdentifier();

            String uploadFilePath = DirectoryPathConstants.UPLOAD_DIR + fileIdentifier;
            File file = new File(uploadFilePath);

            if (!file.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            String downloadFilePath = DirectoryPathConstants.DOWNLOAD_DIR + fileIdentifier;
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
