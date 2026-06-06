package com.example.jbbackend.domain.review.dto;

import jakarta.validation.constraints.NotBlank;

public record ReviewSubmitRequest(
    String reviewStatus,

    String reviewComments,

    @NotBlank(message = "reviewReports는 필수입니다.")
    String reviewReports
) {
}
