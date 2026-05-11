package com.journal.backend.controller;

import com.journal.backend.entity.Article;
import com.journal.backend.entity.User;
import com.journal.backend.repository.ArticleRepository;
import com.journal.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/articles")
@CrossOrigin
public class FileController {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    // POST /api/articles/upload — автор загружает PDF
    @PostMapping("/upload")
    public Map<String, Object> uploadArticle(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("topic") String topic,
            @RequestParam("faculty") String faculty,
            @RequestParam("authorId") Long authorId) throws IOException {

        if (!file.getContentType().equals("application/pdf")) {
            throw new RuntimeException("Только PDF файлы разрешены");
        }

        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new RuntimeException("Автор не найден"));

        Article article = new Article();
        article.setAuthor(author);
        article.setTitle(title);
        article.setTopic(topic);
        article.setFaculty(faculty);
        article.setContent("PDF документ: " + file.getOriginalFilename());
        article.setFilePath(fileName);
        article.setStatus("PENDING");
        article.setCreatedAt(LocalDateTime.now());
        article.setUpdatedAt(LocalDateTime.now());

        Article saved = articleRepository.save(article);

        return Map.of(
                "id", saved.getId(),
                "title", saved.getTitle(),
                "filePath", fileName,
                "message", "Статья успешно загружена"
        );
    }

    // GET /api/articles/{id}/file — открыть PDF
    @GetMapping("/{id}/file")
    public ResponseEntity<Resource> getFile(@PathVariable Long id) throws MalformedURLException {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Статья не найдена"));

        if (article.getFilePath() == null) {
            throw new RuntimeException("У этой статьи нет PDF файла");
        }

        Path filePath = Paths.get(uploadDir).resolve(article.getFilePath());
        Resource resource = new UrlResource(filePath.toUri());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + article.getFilePath() + "\"")
                .body(resource);
    }

    // PUT /api/articles/{id}/resubmit-pdf — автор загружает исправленный PDF
    @PutMapping("/{id}/resubmit-pdf")
    public Map<String, String> resubmitPdf(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) throws IOException {

        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Статья не найдена"));

        if (article.getFilePath() != null) {
            Path oldFile = Paths.get(uploadDir).resolve(article.getFilePath());
            Files.deleteIfExists(oldFile);
        }

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir).resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        article.setFilePath(fileName);
        article.setStatus("PENDING");
        article.setUpdatedAt(LocalDateTime.now());
        articleRepository.save(article);

        return Map.of("message", "Статья отправлена на повторную проверку");
    }
}