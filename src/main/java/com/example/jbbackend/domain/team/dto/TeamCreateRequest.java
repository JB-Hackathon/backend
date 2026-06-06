package com.example.jbbackend.domain.team.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TeamCreateRequest(
        @NotBlank(message = "팀 이름은 필수입니다.")
        @Size(max = 20, message = "팀 이름은 20자 이하여야 합니다.")
        String name
) {
}
