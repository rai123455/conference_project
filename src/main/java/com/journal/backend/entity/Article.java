package com.journal.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "articles")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne                        // много статей → один автор
    @JoinColumn(name = "author_id")   // это внешний ключ в таблице
    private User author;

    @ManyToOne
    @JoinColumn(name = "reviewer_id")
    private User reviewer;   // кто назначен рецензентом

    @Column(nullable = false)
    private String title;

    private String topic;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private String status;  // DRAFT, PENDING, UNDER_REVIEW, PUBLISHED, REVISION

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "file_path")
    private String filePath;

    @Column
    private String faculty;  // факультет статьи

    @Column(name = "chair_decision")
    private String chairDecision;  // решение председателя

    @Column(name = "chair_comment")
    private String chairComment;   // комментарий председателя

    // Три рецензента
    @ManyToOne
    @JoinColumn(name = "reviewer2_id")
    private User reviewer2;

    @ManyToOne
    @JoinColumn(name = "reviewer3_id")
    private User reviewer3;

    // Геттеры и сеттеры
    public String getFaculty() { return faculty; }
    public void setFaculty(String faculty) { this.faculty = faculty; }

    public String getChairDecision() { return chairDecision; }
    public void setChairDecision(String chairDecision) { this.chairDecision = chairDecision; }

    public String getChairComment() { return chairComment; }
    public void setChairComment(String chairComment) { this.chairComment = chairComment; }

    public User getReviewer2() { return reviewer2; }
    public void setReviewer2(User reviewer2) { this.reviewer2 = reviewer2; }

    public User getReviewer3() { return reviewer3; }
    public void setReviewer3(User reviewer3) { this.reviewer3 = reviewer3; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    // Геттеры и сеттеры (правая кнопка → Generate → Getters and Setters)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getAuthor() { return author; }
    public void setAuthor(User author) { this.author = author; }

    public User getReviewer() { return reviewer; }
    public void setReviewer(User reviewer) { this.reviewer = reviewer; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}