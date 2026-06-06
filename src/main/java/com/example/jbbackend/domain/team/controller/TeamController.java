package com.example.jbbackend.domain.team.controller;

import com.example.jbbackend.domain.team.dto.TeamCreateRequest;
import com.example.jbbackend.domain.team.dto.TeamResponse;
import com.example.jbbackend.domain.team.dto.TeamUpdateRequest;
import com.example.jbbackend.domain.team.service.TeamService;
import com.example.jbbackend.global.Exception.ErrorCode;
import com.example.jbbackend.global.common.response.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/teams")
public class TeamController {

    private final TeamService teamService;

    @PostMapping({"", "/"})
    public ResponseEntity<ApiResponse<TeamResponse>> createTeam(
        @Valid @RequestBody TeamCreateRequest request
    ) {
        TeamResponse response = teamService.createTeam(request);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(response, HttpStatus.CREATED));
    }

    @PatchMapping("/{teamId}")
    public ResponseEntity<ApiResponse<TeamResponse>> patchTeam(
        @PathVariable Long teamId,
        @Valid @RequestBody TeamUpdateRequest request
    ) {
        return teamService.updateTeam(teamId, request)
            .map(response -> ResponseEntity.ok(ApiResponse.success(response)))
            .orElseGet(this::teamNotFound);
}

    @PutMapping("/{teamId}")
    public ResponseEntity<ApiResponse<TeamResponse>> putTeam(
        @PathVariable Long teamId,
        @Valid @RequestBody TeamUpdateRequest request
    ) {
        return patchTeam(teamId, request);
    }

    @DeleteMapping("/{teamId}")
    public ResponseEntity<ApiResponse<Void>> deleteTeam(@PathVariable Long teamId) {
        if (!teamService.deleteTeam(teamId)) {
            return teamNotFound();
        }
        return ResponseEntity.ok(ApiResponse.success());
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<ApiResponse<TeamResponse>> getTeam(@PathVariable Long teamId) {
        return teamService.getTeam(teamId)
            .map(response -> ResponseEntity.ok(ApiResponse.success(response)))
            .orElseGet(this::teamNotFound);
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<TeamResponse>>> getTeams() {
        return ResponseEntity.ok(ApiResponse.success(teamService.getTeams()));
    }

    private <T> ResponseEntity<ApiResponse<T>> teamNotFound() {
        return ResponseEntity
            .status(ErrorCode.TEAM_NOT_FOUND.getStatus())
            .body(ApiResponse.error(ErrorCode.TEAM_NOT_FOUND));
    }
}
