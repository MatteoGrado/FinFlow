package de.grado.finflow.repository;

import de.grado.finflow.model.OcrDocument;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OcrDocumentRepository extends JpaRepository<OcrDocument, Long>
{
}
