package org.application.repository;

import org.application.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByEmail(String email);

    @Query("""
        SELECT u
        FROM UserEntity u
        WHERE (u.username = :username AND :username IS NOT NULL)
        OR (u.email = :email AND :email IS NOT NULL)
""") Optional<UserEntity> findByUsernameOrEmail(@Param ("username") String username,
                                               @Param ("email") String email);

    @Query("""
      SELECT u.id
      FROM UserEntity u
      WHERE u.role = 'USER' AND u.username = :username
      """)
    Optional<Long> findPublicUserByUsername(@Param("username") String username);

}

