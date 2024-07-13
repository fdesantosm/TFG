package org.application.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "files", schema = "public")
public class FileEntity {

    @Id
    @SequenceGenerator(name = "files_seq_generator", sequenceName = "files_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "files_seq_generator")
    @Column(name = "id")
    private Long id;

    @Size(min = 5, max = 50)
    @Column(name = "title", unique = true, nullable = false)
    private String title;

    @Size(max= 255)
    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @Column(name = "public_Token", nullable = true)
    private String publicToken;

    @Column(name = "visible", nullable = false)
    private boolean visible;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
}
