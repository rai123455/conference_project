package com.journal.backend.repository;

import com.journal.backend.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param; // импорты для трех рецензента

public interface ArticleRepository extends JpaRepository<Article, Long> {

    // Все статьи конкретного автора
    List<Article> findByAuthorId(Long authorId);

    // Все статьи с определённым статусом
    List<Article> findByStatus(String status);

    // Статьи назначенные конкретному рецензенту с нужным статусом
    List<Article> findByReviewerIdAndStatus(Long reviewerId, String status);

    // Статьи по факультету и статусу — для председателя
    List<Article> findByFacultyAndStatus(String faculty, String status);

    // Все статьи факультета
    List<Article> findByFaculty(String faculty);

    // Статьи назначенные любому из трёх рецензентов
    List<Article> findByReviewer_IdAndStatus(Long reviewerId, String status);

    // Статьи где пользователь назначен любым из трёх рецензентов
    @Query("SELECT a FROM Article a WHERE " +
            "(a.reviewer.id = :reviewerId OR " +
            "a.reviewer2.id = :reviewerId OR " +
            "a.reviewer3.id = :reviewerId) " +
            "AND a.status = 'UNDER_REVIEW'")
    List<Article> findByAnyReviewerAndStatus(@Param("reviewerId") Long reviewerId);
}