package org.application.service;

import org.application.entity.FileEntity;
import org.application.entity.UserEntity;
import org.application.entity.out.FileDto;

import java.util.List;

public interface FileService {

    FileEntity createFileEntity(String title, String fileName, String description, UserEntity user);

    public List<FileDto> findPublicFilesFromUser(String username);

}
