package org.application.service.impl;

import org.application.entity.FileToken;
import org.application.repository.FileTokenRepository;
import org.application.service.FileTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class FileTokenServiceImpl implements FileTokenService {

    @Autowired
    private FileTokenRepository fileTokenRepository;

    public FileToken createFileToken(String fileIdentifier, long durationInMinutes) {
        String token = UUID.randomUUID().toString();
        LocalDateTime expirationTime = LocalDateTime.now().plusHours(durationInMinutes);
        FileToken fileToken = FileToken.builder()
                .token(token)
                .fileIdentifier(fileIdentifier)
                .expirationTime(expirationTime)
                .build();
        return fileTokenRepository.save(fileToken);
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