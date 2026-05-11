package com.journal.backend.dto;

public class AssignThreeReviewersRequest {
    private Long articleId;
    private Long reviewer1Id;
    private Long reviewer2Id;
    private Long reviewer3Id;

    public Long getArticleId() { return articleId; }
    public void setArticleId(Long articleId) { this.articleId = articleId; }

    public Long getReviewer1Id() { return reviewer1Id; }
    public void setReviewer1Id(Long reviewer1Id) { this.reviewer1Id = reviewer1Id; }

    public Long getReviewer2Id() { return reviewer2Id; }
    public void setReviewer2Id(Long reviewer2Id) { this.reviewer2Id = reviewer2Id; }

    public Long getReviewer3Id() { return reviewer3Id; }
    public void setReviewer3Id(Long reviewer3Id) { this.reviewer3Id = reviewer3Id; }
}