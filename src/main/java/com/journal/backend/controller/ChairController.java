package com.journal.backend.controller;

import com.journal.backend.dto.AssignThreeReviewersRequest;
import com.journal.backend.dto.ChairDecisionRequest;
import com.journal.backend.entity.Article;
import com.journal.backend.entity.Review;
import com.journal.backend.entity.User;
import com.journal.backend.repository.UserRepository;
import com.journal.backend.service.ChairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chair")
@CrossOrigin
public class ChairController {

    @Autowired
    private ChairService chairService;

    @Autowired
    private UserRepository userRepository;

    // GET /api/chair/articles/pending?faculty=Инженерный факультет
    @GetMapping("/articles/pending")
    public List<Article> getPending(@RequestParam String faculty) {
        return chairService.getIncomingForChair(faculty);
    }

    // GET /api/chair/articles/under-review?faculty=Инженерный факультет
    @GetMapping("/articles/under-review")
    public List<Article> getUnderReview(@RequestParam String faculty) {
        return chairService.getUnderReviewForChair(faculty);
    }

    // GET /api/chair/articles/all?faculty=Инженерный факультет
    @GetMapping("/articles/all")
    public List<Article> getAll(@RequestParam String faculty) {
        return chairService.getAllArticlesForChair(faculty);
    }

    // POST /api/chair/assign-three — назначить трёх рецензентов
    @PostMapping("/assign-three")
    public Article assignThree(@RequestBody AssignThreeReviewersRequest request) {
        return chairService.assignThreeReviewers(request);
    }

    // GET /api/chair/articles/{id}/reviews — рецензии на статью
    @GetMapping("/articles/{id}/reviews")
    public List<Review> getReviews(@PathVariable Long id) {
        return chairService.getReviewsForArticle(id);
    }

    // POST /api/chair/decision — финальное решение председателя
    @PostMapping("/decision")
    public Article makeDecision(@RequestBody ChairDecisionRequest request) {
        return chairService.makeDecision(request);
    }

    // GET /api/chair/reviewers — список рецензентов для назначения
    @GetMapping("/reviewers")
    public List<User> getReviewers() {
        return userRepository.findByRole("REVIEWER");
    }
}