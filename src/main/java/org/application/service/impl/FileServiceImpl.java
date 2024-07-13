package org.application.service.impl;

import org.application.entity.FileEntity;
import org.application.entity.UserEntity;
import org.application.repository.FileRepository;
import org.application.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileServiceImpl implements FileService {
    FileRepository fileRepository;

    @Autowired
    public FileServiceImpl(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
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
}
