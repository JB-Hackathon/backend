package com.example.jbbackend.domain.board.dto;

import com.example.jbbackend.domain.review.dto.ReviewContentVersionResponse;

public record ReviewAiStartResponse(
    ReviewBoardResponse reviewBoard,
    ReviewContentVersionResponse latestVersion,
    AiReviewResponse aiReview
) {
}
