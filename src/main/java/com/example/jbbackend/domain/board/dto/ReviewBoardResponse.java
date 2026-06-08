package com.example.jbbackend.domain.board.dto;

import com.example.jbbackend.domain.board.entity.ReviewBoard;
import java.time.LocalDateTime;

public record ReviewBoardResponse(
    Long reviewId,
    Long contentCreatorId,
    Long complianceAdvisorId,
    String managementNumber,
    String reviewApprovalNumber,
    String title,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

    public static ReviewBoardResponse from(ReviewBoard board) {
        return new ReviewBoardResponse(
            board.getId(),
            board.getContentCreator().getId(),
            board.getComplianceAdvisor().getId(),
            board.getManagementNumber(),
            board.getReviewApprovalNumber(),
            board.getTitle(),
            board.getCreatedAt(),
            board.getUpdatedAt()
        );
    }
}
