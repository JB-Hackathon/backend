package com.example.jbbackend.domain.review.dto;

import com.example.jbbackend.domain.review.entity.ReviewContentVersion;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

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
    List<String> contentFilePaths,
    List<String> contentFileUrls,
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
            parseFilePaths(review.getContentFilePath()),
            parseFilePaths(review.getContentFilePath()).stream()
                .map(ReviewContentVersionResponse::toFileUrl)
                .toList(),
            review.getContentText(),
            review.getContentDescription(),
            review.getReviewStatus().name(),
            review.getReviewComments(),
            review.getReviewReports(),
            review.getCreatedAt(),
            review.getUpdatedAt()
        );
    }

    private static List<String> parseFilePaths(String contentFilePath) {
        if (contentFilePath == null || contentFilePath.isBlank()) {
            return List.of();
        }
        return Arrays.stream(contentFilePath.split(","))
            .map(String::trim)
            .filter(path -> !path.isBlank())
            .toList();
    }

    private static String toFileUrl(String contentFilePath) {
        int slashIndex = contentFilePath.lastIndexOf('/');
        String filename = slashIndex < 0 ? contentFilePath : contentFilePath.substring(slashIndex + 1);
        return "/api/v1/files/reviews/" + filename;
    }
}
