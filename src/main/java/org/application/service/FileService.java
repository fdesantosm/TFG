package org.application.service;

import org.application.entity.FileEntity;
import org.application.entity.UserEntity;

public interface FileService {

    FileEntity createFileEntity(String title, String fileName, String description, UserEntity user);
}
