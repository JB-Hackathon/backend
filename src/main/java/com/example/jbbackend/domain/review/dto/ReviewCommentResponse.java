package com.example.jbbackend.domain.review.dto;

import com.example.jbbackend.domain.review.entity.ReviewContentVersion;

public record ReviewCommentResponse(
    Long reviewId,
    Long contentId,
    String reviewComments
) {

    public static ReviewCommentResponse from(ReviewContentVersion review) {
        return new ReviewCommentResponse(
            review.getBoard().getId(),
            review.getId(),
            review.getReviewComments()
        );
    }
}
