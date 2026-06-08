package com.example.jbbackend.domain.board.dto;

import com.example.jbbackend.domain.board.entity.ReviewBoard;
import com.example.jbbackend.domain.review.entity.ReviewContentVersion;
import java.time.LocalDateTime;

public record ReviewBoardResponse(
    Long reviewId,
    Long contentCreatorId,
    String contentCreatorName,
    Long complianceAdvisorId,
    String complianceAdvisorName,
    String managementNumber,
    String reviewApprovalNumber,
    String title,
    String channelType,
    String contentType,
    String reviewStatus,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

    public static ReviewBoardResponse from(ReviewBoard board) {
        return from(board, null);
    }

    public static ReviewBoardResponse from(ReviewBoard board, ReviewContentVersion latestVersion) {
        return new ReviewBoardResponse(
            board.getId(),
            board.getContentCreator().getId(),
            board.getContentCreator().getName(),
            board.getComplianceAdvisor().getId(),
            board.getComplianceAdvisor().getName(),
            board.getManagementNumber(),
            board.getReviewApprovalNumber(),
            board.getTitle(),
            latestVersion == null ? null : latestVersion.getChannelType().name(),
            latestVersion == null ? null : latestVersion.getContentType().name(),
            latestVersion == null ? null : latestVersion.getReviewStatus().name(),
            board.getCreatedAt(),
            board.getUpdatedAt()
        );
    }
}
