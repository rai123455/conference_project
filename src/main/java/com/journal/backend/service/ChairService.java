package com.journal.backend.service;

import com.journal.backend.dto.AssignThreeReviewersRequest;
import com.journal.backend.dto.ChairDecisionRequest;
import com.journal.backend.entity.Article;
import com.journal.backend.entity.Review;
import com.journal.backend.entity.User;
import com.journal.backend.repository.ArticleRepository;
import com.journal.backend.repository.ReviewRepository;
import com.journal.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChairService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    // Получить входящие статьи для председателя по факультету
    public List<Article> getIncomingForChair(String faculty) {
        return articleRepository.findByFacultyAndStatus(faculty, "PENDING");
    }

    // Получить статьи на рецензировании
    public List<Article> getUnderReviewForChair(String faculty) {
        return articleRepository.findByFacultyAndStatus(faculty, "UNDER_REVIEW");
    }

    // Председатель назначает трёх рецензентов
    public Article assignThreeReviewers(AssignThreeReviewersRequest request) {
        Article article = articleRepository.findById(request.getArticleId())
                .orElseThrow(() -> new RuntimeException("Статья не найдена"));

        User r1 = userRepository.findById(request.getReviewer1Id())
                .orElseThrow(() -> new RuntimeException("Рецензент 1 не найден"));
        User r2 = userRepository.findById(request.getReviewer2Id())
                .orElseThrow(() -> new RuntimeException("Рецензент 2 не найден"));
        User r3 = userRepository.findById(request.getReviewer3Id())
                .orElseThrow(() -> new RuntimeException("Рецензент 3 не найден"));

        article.setReviewer(r1);
        article.setReviewer2(r2);
        article.setReviewer3(r3);
        article.setStatus("UNDER_REVIEW");
        article.setUpdatedAt(LocalDateTime.now());

        return articleRepository.save(article);
    }

    // Получить рецензии на статью (для председателя)
    public List<Review> getReviewsForArticle(Long articleId) {
        return reviewRepository.findByArticleIdOrderByReviewerNumber(articleId);
    }

    // Председатель делает финальное решение
    public Article makeDecision(ChairDecisionRequest request) {
        Article article = articleRepository.findById(request.getArticleId())
                .orElseThrow(() -> new RuntimeException("Статья не найдена"));

        article.setChairDecision(request.getDecision());
        article.setChairComment(request.getComment());
        article.setUpdatedAt(LocalDateTime.now());

        if (request.getDecision().equals("ACCEPTED")) {
            article.setStatus("PUBLISHED");
        } else if (request.getDecision().equals("REVISION")) {
            article.setStatus("REVISION");
        } else {
            article.setStatus("REJECTED");
        }

        return articleRepository.save(article);
    }

    // Все статьи факультета председателя
    public List<Article> getAllArticlesForChair(String faculty) {
        return articleRepository.findByFaculty(faculty);
    }
}