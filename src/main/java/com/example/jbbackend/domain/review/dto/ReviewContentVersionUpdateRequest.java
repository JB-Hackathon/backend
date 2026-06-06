package com.example.jbbackend.domain.review.dto;

public record ReviewContentVersionUpdateRequest(
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
    String reviewReports
) {
}
