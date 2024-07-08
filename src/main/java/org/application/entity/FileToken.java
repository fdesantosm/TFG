package org.application.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "file_tokens", schema = "public")
public class FileToken {

    @Id
    @SequenceGenerator(name = "users_seq_generator", sequenceName = "users_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq_generator")
    private Long id;

    @Column(name = "token", nullable = false, unique = true)
    private String token;

    @Column(name = "file_identifier", nullable = false)
    private String fileIdentifier;

    @Column(name = "expiration_time", nullable = false)
    private LocalDateTime expirationTime;
}