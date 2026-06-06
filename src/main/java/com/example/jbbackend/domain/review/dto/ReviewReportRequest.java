package com.example.jbbackend.domain.review.dto;

import jakarta.validation.constraints.NotBlank;

public record ReviewReportRequest(
    @NotBlank(message = "reviewReports는 필수입니다.")
    String reviewReports
) {
}
