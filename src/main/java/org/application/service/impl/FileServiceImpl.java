package org.application.service.impl;

import org.application.entity.FileEntity;
import org.application.entity.UserEntity;
import org.application.entity.out.FileDto;
import org.application.repository.FileRepository;
import org.application.repository.UserRepository;
import org.application.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FileServiceImpl implements FileService {

    FileRepository fileRepository;

    UserRepository userRepository;

    @Autowired
    public FileServiceImpl(FileRepository fileRepository, UserRepository userRepository) {
        this.fileRepository = fileRepository;
        this.userRepository = userRepository;
    }

    @Override
    public FileEntity createFileEntity(String title, String fileName, String description, UserEntity user) {
        FileEntity file = FileEntity.builder()
                .title(title)
                .fileName(fileName)
                .description(description)
                .visible(false)
                .user(user)
                .build();
        fileRepository.save(file);
        return file;
    }

    @Override
    public List<FileDto> findPublicFilesFromUser(String username) {

        Long userId = userRepository.findPublicUserByUsername(username)
          .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return Optional.of(fileRepository.findPublicFilesByUsername(userId))
          .filter(files -> !files.isEmpty())
          .orElseThrow(() -> new RuntimeException("No se encontraron archivos públicos para el usuario"));

    }
}
