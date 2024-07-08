package org.application.service;

import org.application.entity.FileToken;

public interface FileTokenService {

    public FileToken createFileToken(String fileIdentifier, long durationInMinutes);

    public FileToken validateToken(String token);
}
