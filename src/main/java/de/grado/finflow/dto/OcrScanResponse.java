package de.grado.finflow.dto;

import de.grado.finflow.model.OcrDocumentStatus;

import java.time.Instant;

public record OcrScanResponse(
        Long id,
        String originalFilename,
        String storedFilename,
        String storagePath,
        String ocrText,
        OcrDocumentStatus status,
        Instant createdAt,
        Instant processedAt
)
{
}
