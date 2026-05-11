package com.journal.backend.dto;

public class ChairDecisionRequest {
    private Long articleId;
    private String decision;  // ACCEPTED, REVISION, REJECTED
    private String comment;

    public Long getArticleId() { return articleId; }
    public void setArticleId(Long articleId) { this.articleId = articleId; }

    public String getDecision() { return decision; }
    public void setDecision(String decision) { this.decision = decision; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}