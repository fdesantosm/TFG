package org.application.service.impl;

import org.application.entity.FileToken;
import org.application.entity.UserEntity;
import org.application.repository.FileRepository;
import org.application.repository.FileTokenRepository;
import org.application.service.FileTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class FileTokenServiceImpl implements FileTokenService {

    private FileTokenRepository fileTokenRepository;
    private FileRepository fileRepository;

    @Autowired
    public FileTokenServiceImpl(FileTokenRepository fileTokenRepository, FileRepository fileRepository) {
        this.fileTokenRepository = fileTokenRepository;
        this.fileRepository = fileRepository;
    }

    public FileToken createFileToken(String title, long durationInHours, UserEntity authentedUser) {
        if (durationInHours < 1) {
            throw new IllegalArgumentException("La duración debe ser al menos de una hora.");
        }
        var existingFile = fileRepository.findByTitle(title)
                .orElseThrow(() -> new IllegalArgumentException("Archivo con el título " + title + " no encontrado."));

        if (authentedUser.getId().equals(existingFile.getUser().getId())){
            String token = UUID.randomUUID().toString();
            LocalDateTime expirationTime = LocalDateTime.now().plusHours(durationInHours);
            FileToken fileToken = FileToken.builder()
                    .token(token)
                    .expirationTime(expirationTime)
                    .fileEntity(existingFile)
                    .build();
            return fileTokenRepository.save(fileToken);
        }
        else{
            throw new IllegalArgumentException("Este usuario no esta autorizado para crear tokens de este archivo.");
        }

    }

    public FileToken validateToken(String token) {
        FileToken fileToken = fileTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));

        if (fileToken.getExpirationTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Token has expired");
        }

        return fileToken;
    }
}