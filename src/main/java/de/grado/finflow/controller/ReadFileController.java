package de.grado.finflow.controller;

import de.grado.finflow.dto.OcrScanResponse;
import de.grado.finflow.service.OCRScanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class ReadFileController
{
    private final OCRScanService ocrScanService;

    @PostMapping("/scanFile")
    public OcrScanResponse scanFile(@RequestParam("file") MultipartFile file)
    {
        return ocrScanService.scanFile(file);
    }

    @GetMapping("/scanFile")
    public List<OcrScanResponse> getScans()
    {
        return ocrScanService.getAllScans();
    }

    @GetMapping("/scanFile/{id}")
    public OcrScanResponse getScanById(@PathVariable Long id)
    {
        return ocrScanService.getScanById(id);
    }
}
