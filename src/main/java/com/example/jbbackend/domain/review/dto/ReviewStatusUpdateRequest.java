package com.example.jbbackend.domain.review.dto;

import jakarta.validation.constraints.NotBlank;

public record ReviewStatusUpdateRequest(
    @NotBlank(message = "reviewStatus는 필수입니다.")
    String reviewStatus,

    String reviewApprovalNumber
) {
}
