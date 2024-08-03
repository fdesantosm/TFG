package org.application.service;

import org.application.entity.FileToken;
import org.application.entity.UserEntity;

public interface FileTokenService {

    public FileToken createFileToken(String title, long durationInHours, UserEntity authentedUser);

    public FileToken validateToken(String token);
}
