package com.example.jbbackend.domain.review.dto;

import com.example.jbbackend.domain.review.entity.ReviewContentVersion;

public record ReviewFeedbackResponse(
    Long reviewId,
    Long contentId,
    Integer versionNo,
    String reviewStatus,
    String reviewComments,
    String reviewReports
) {

    public static ReviewFeedbackResponse from(ReviewContentVersion review) {
        return new ReviewFeedbackResponse(
            review.getBoard().getId(),
            review.getId(),
            review.getVersionNo(),
            review.getReviewStatus().name(),
            review.getReviewComments(),
            review.getReviewReports()
        );
    }
}
