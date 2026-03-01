package org.application.repository;

import org.application.entity.FileEntity;
import org.application.entity.out.FileDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
  Optional<FileEntity> findByFileName(String fileName);

  Optional<FileEntity> findByTitle(String title);

  @Query("""
    SELECT
      f.user.id AS id,
      f.title,
      f.description,
      f.publicToken
    FROM FileEntity f
      JOIN f.user u
    WHERE f.visible = true AND u.id = :userId
    """)
  List<FileDto> findPublicFilesByUsername(Long userId);
}
