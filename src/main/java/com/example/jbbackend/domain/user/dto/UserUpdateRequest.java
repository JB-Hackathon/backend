package com.example.jbbackend.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @Size(max = 255, message = "이메일은 255자 이하여야 합니다.")
    String email,

    @Size(min = 8, max = 255, message = "비밀번호는 8자 이상 255자 이하여야 합니다.")
    String password,

    @Size(max = 20, message = "이름은 20자 이하여야 합니다.")
    String name,

    String role,

    Long teamId
) {
}
