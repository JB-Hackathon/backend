package com.example.jbbackend.global.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/files")
public class FileController {

    private static final Path REVIEW_UPLOAD_DIRECTORY = Path.of("uploads", "reviews").normalize();

    @GetMapping("/reviews/{filename}")
    public ResponseEntity<Resource> getReviewFile(@PathVariable String filename) throws IOException {
        Path filePath = REVIEW_UPLOAD_DIRECTORY.resolve(filename).normalize();
        if (!filePath.startsWith(REVIEW_UPLOAD_DIRECTORY) || !Files.exists(filePath)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Resource resource = new UrlResource(filePath.toUri());
        if (!resource.exists() || !resource.isReadable()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        String contentType = Files.probeContentType(filePath);
        MediaType mediaType = contentType == null
            ? MediaType.APPLICATION_OCTET_STREAM
            : MediaType.parseMediaType(contentType);

        return ResponseEntity.ok()
            .contentType(mediaType)
            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
            .body(resource);
    }
}
