package org.application.repository;

import org.application.entity.FileToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileTokenRepository extends JpaRepository<FileToken, Long> {
    Optional<FileToken> findByToken(String token);
}