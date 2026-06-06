package com.example.jbbackend.domain.board.dto;

import com.example.jbbackend.domain.review.dto.ReviewContentVersionResponse;

public record ReviewStartResponse(
    ReviewBoardResponse reviewBoard,
    ReviewContentVersionResponse latestVersion
) {
}
