package com.example.jbbackend.domain.team.service;

import com.example.jbbackend.domain.team.dto.TeamCreateRequest;
import com.example.jbbackend.domain.team.dto.TeamResponse;
import com.example.jbbackend.domain.team.dto.TeamUpdateRequest;
import com.example.jbbackend.domain.team.entity.Team;
import com.example.jbbackend.domain.team.repository.TeamRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamService {

    private final TeamRepository teamRepository;

    @Transactional
    public TeamResponse createTeam(TeamCreateRequest request) {
        Team team = Team.create(request.name());
        return TeamResponse.from(teamRepository.save(team));
    }

    @Transactional
    public Optional<TeamResponse> updateTeam(Long teamId, TeamUpdateRequest request) {
        return teamRepository.findByIdAndDeletedAtIsNull(teamId)
            .map(team -> {
                team.updateName(request.name());
                return TeamResponse.from(team);
            });
    }

    @Transactional
    public boolean deleteTeam(Long teamId) {
        return teamRepository.findByIdAndDeletedAtIsNull(teamId)
            .map(team -> {
                team.delete();
                return true;
            })
            .orElse(false);
    }

    public Optional<TeamResponse> getTeam(Long teamId) {
        return teamRepository.findByIdAndDeletedAtIsNull(teamId)
            .map(TeamResponse::from);
    }

    public List<TeamResponse> getTeams() {
        return teamRepository.findAllByDeletedAtIsNullOrderByIdAsc()
            .stream()
            .map(TeamResponse::from)
            .toList();
    }
}
