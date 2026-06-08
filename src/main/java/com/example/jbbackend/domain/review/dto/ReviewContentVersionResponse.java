package com.example.jbbackend.domain.review.dto;

import com.example.jbbackend.domain.review.entity.ReviewContentVersion;
import java.time.LocalDateTime;

public record ReviewContentVersionResponse(
    Long reviewId,
    Long boardId,
    Integer versionNo,
    String businessSector,
    String channelType,
    String contentType,
    String contentCategory,
    String productCategory,
    String languageCode,
    String contentFilePath,
    String contentText,
    String contentDescription,
    String reviewStatus,
    String reviewComments,
    String reviewReports,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

    public static ReviewContentVersionResponse from(ReviewContentVersion review) {
        return new ReviewContentVersionResponse(
            review.getId(),
            review.getBoard().getId(),
            review.getVersionNo(),
            review.getBusinessSector().name(),
            review.getChannelType().name(),
            review.getContentType().name(),
            review.getContentCategory().name(),
            review.getProductCategory() == null ? null : review.getProductCategory().name(),
            review.getLanguageCode().name(),
            review.getContentFilePath(),
            review.getContentText(),
            review.getContentDescription(),
            review.getReviewStatus().name(),
            review.getReviewComments(),
            review.getReviewReports(),
            review.getCreatedAt(),
            review.getUpdatedAt()
        );
    }
}
