package com.example.jbbackend.domain.board.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ReviewBoardCreateRequest(
    @NotNull(message = "contentCreatorId는 필수입니다.")
    Long contentCreatorId,

    @NotNull(message = "complianceAdvisorId는 필수입니다.")
    Long complianceAdvisorId,

    @NotBlank(message = "managementNumber는 필수입니다.")
    @Size(max = 50, message = "managementNumber는 50자 이하여야 합니다.")
    String managementNumber,

    @Size(max = 50, message = "reviewApprovalNumber는 50자 이하여야 합니다.")
    String reviewApprovalNumber,

    @NotBlank(message = "title은 필수입니다.")
    @Size(max = 255, message = "title은 255자 이하여야 합니다.")
    String title,

    @NotBlank(message = "businessSector는 필수입니다.")
    String businessSector,

    @NotBlank(message = "channelType은 필수입니다.")
    String channelType,

    @NotBlank(message = "contentType은 필수입니다.")
    String contentType,

    @NotBlank(message = "contentCategory는 필수입니다.")
    String contentCategory,

    String productCategory,

    @NotBlank(message = "languageCode는 필수입니다.")
    String languageCode,

    String contentFilePath,
    String contentText,
    String contentDescription
) {
}
