package com.example.jbbackend.domain.team.dto;

import com.example.jbbackend.domain.team.entity.Team;
import java.time.LocalDateTime;

public record TeamResponse(
        Long teamId,
        String name,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static TeamResponse from(Team team) {
        return new TeamResponse(
                team.getId(),
                team.getName(),
                team.getCreatedAt(),
                team.getUpdatedAt()
        );
    }
}
