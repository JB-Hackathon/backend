package com.example.jbbackend.domain.review.dto;

import com.example.jbbackend.domain.board.entity.ReviewBoard;
import com.example.jbbackend.domain.review.entity.ReviewContentVersion;
import java.util.Arrays;
import java.util.List;

public record ReviewDetailResponse(
    Long reviewId,
    Long contentId,
    String title,
    String reviewStatus,
    String managementNumber,
    Long contentCreatorId,
    String contentCreatorName,
    Long complianceAdvisorId,
    String complianceAdvisorName,
    String reviewApprovalNumber,
    String contentText,
    String contentFilePath,
    List<String> contentFilePaths,
    List<String> contentFileUrls
) {

    public static ReviewDetailResponse from(ReviewBoard board, ReviewContentVersion latestVersion) {
        String contentFilePath = latestVersion == null ? null : latestVersion.getContentFilePath();
        List<String> contentFilePaths = parseFilePaths(contentFilePath);

        return new ReviewDetailResponse(
            board.getId(),
            latestVersion == null ? null : latestVersion.getId(),
            board.getTitle(),
            latestVersion == null ? null : latestVersion.getReviewStatus().name(),
            board.getManagementNumber(),
            board.getContentCreator().getId(),
            board.getContentCreator().getName(),
            board.getComplianceAdvisor().getId(),
            board.getComplianceAdvisor().getName(),
            board.getReviewApprovalNumber(),
            latestVersion == null ? null : latestVersion.getContentText(),
            contentFilePath,
            contentFilePaths,
            contentFilePaths.stream()
                .map(ReviewDetailResponse::toFileUrl)
                .toList()
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
