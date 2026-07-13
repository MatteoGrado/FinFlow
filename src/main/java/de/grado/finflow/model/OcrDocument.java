package de.grado.finflow.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "ocr_documents")
@Getter
@Setter
public class OcrDocument
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String originalFilename;

    @Column(nullable = false, unique = true)
    private String storedFilename;

    @Column(nullable = false)
    private String storagePath;

    private String contentType;

    private Long fileSize;

    @Column(columnDefinition = "text")
    private String ocrText;

    @Column(columnDefinition = "text")
    private String errorMessage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OcrDocumentStatus status;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    private Instant processedAt;

    @PrePersist
    void onCreate()
    {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }
}
