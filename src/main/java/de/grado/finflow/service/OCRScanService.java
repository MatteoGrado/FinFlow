package de.grado.finflow.service;

import de.grado.finflow.dto.OcrScanResponse;
import de.grado.finflow.model.OcrDocument;
import de.grado.finflow.model.OcrDocumentStatus;
import de.grado.finflow.repository.OcrDocumentRepository;
import io.sentry.Sentry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.UUID;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OCRScanService
{
    private final OcrDocumentRepository ocrDocumentRepository;

    @Value("${app.ocr.storage-dir}")
    private String storageDir;

    public OcrScanResponse scanFile(MultipartFile file)
    {
        OcrDocument document = null;
        try {
            Path storageDirectory = Path.of(storageDir);
            Files.createDirectories(storageDirectory);

            String originalFilename = file.getOriginalFilename() == null ? "upload" : file.getOriginalFilename();
            String storedFilename = UUID.randomUUID() + "-" + sanitizeFilename(originalFilename);
            Path permanentFilePath = storageDirectory.resolve(storedFilename);

            file.transferTo(permanentFilePath);

            document = new OcrDocument();
            document.setOriginalFilename(originalFilename);
            document.setStoredFilename(storedFilename);
            document.setStoragePath(permanentFilePath.toAbsolutePath().toString());
            document.setContentType(file.getContentType());
            document.setFileSize(file.getSize());
            document.setStatus(OcrDocumentStatus.PENDING);
            document = ocrDocumentRepository.save(document);

            ITesseract tesseract = new Tesseract();
            tesseract.setDatapath("C:/Program Files/Tesseract-OCR/tessdata");
            tesseract.setLanguage("deu");

            String result = tesseract.doOCR(permanentFilePath.toFile());

            document.setOcrText(result);
            document.setStatus(OcrDocumentStatus.COMPLETED);
            document.setProcessedAt(Instant.now());
            document = ocrDocumentRepository.save(document);

            return toResponse(document);
        } catch (RuntimeException e) {
            Sentry.captureException(e);
            if (document != null) {
                document.setStatus(OcrDocumentStatus.FAILED);
                document.setErrorMessage(e.getMessage());
                document.setProcessedAt(Instant.now());
                ocrDocumentRepository.save(document);
            }
            throw e;
        } catch (TesseractException | IOException e) {
            Sentry.captureException(e);
            if (document != null) {
                document.setStatus(OcrDocumentStatus.FAILED);
                document.setErrorMessage(e.getMessage());
                document.setProcessedAt(Instant.now());
                ocrDocumentRepository.save(document);
            }
            throw new RuntimeException(e);
        }
    }

    public List<OcrScanResponse> getAllScans()
    {
        return ocrDocumentRepository.findAll().stream().map(OCRScanService::toResponse).toList();
    }

    public OcrScanResponse getScanById(Long id)
    {
        OcrDocument document = ocrDocumentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("OCR document not found: " + id));
        return toResponse(document);
    }

    private static String sanitizeFilename(String filename)
    {
        return filename.replaceAll("[^a-zA-Z0-9._-]", "_");
    }

    private static OcrScanResponse toResponse(OcrDocument document)
    {
        return new OcrScanResponse(
                document.getId(),
                document.getOriginalFilename(),
                document.getStoredFilename(),
                document.getStoragePath(),
                document.getOcrText(),
                document.getStatus(),
                document.getCreatedAt(),
                document.getProcessedAt()
        );
    }
}
