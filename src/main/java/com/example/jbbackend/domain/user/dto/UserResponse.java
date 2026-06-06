package com.example.jbbackend.domain.user.dto;

import com.example.jbbackend.domain.user.entity.User;
import java.time.LocalDateTime;

public record UserResponse(
    Long userId,
    String email,
    String name,
    String role,
    Long teamId,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

    public static UserResponse from(User user) {
        return new UserResponse(
            user.getId(),
            user.getEmail(),
            user.getName(),
            user.getRole().name(),
            user.getTeamId(),
            user.getCreatedAt(),
            user.getUpdatedAt()
        );
    }
}
