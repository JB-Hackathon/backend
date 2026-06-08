package com.example.jbbackend.domain.auth.service;

import com.example.jbbackend.domain.auth.dto.LoginRequest;
import com.example.jbbackend.domain.auth.dto.RegisterRequest;
import com.example.jbbackend.domain.team.entity.Team;
import com.example.jbbackend.domain.team.repository.TeamRepository;
import com.example.jbbackend.domain.user.dto.UserResponse;
import com.example.jbbackend.domain.user.entity.User;
import com.example.jbbackend.domain.user.entity.UserRole;
import com.example.jbbackend.domain.user.repository.UserRepository;
import com.example.jbbackend.global.Exception.BusinessException;
import com.example.jbbackend.global.Exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;

    @Transactional
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByEmailAndDeletedAtIsNull(request.email())) {
            throw new BusinessException(ErrorCode.EMAIL_DUPLICATION);
        }

        User user = User.create(
            request.email(),
            request.password(),
            request.name(),
            parseRole(request.role()),
            findTeam(request.teamId())
        );
        return UserResponse.from(userRepository.save(user));
    }

    public UserResponse login(LoginRequest request) {
        return userRepository.findByEmailAndDeletedAtIsNull(request.email())
            .filter(user -> user.getPassword().equals(request.password()))
            .map(UserResponse::from)
            .orElseThrow(() -> new BusinessException(ErrorCode.LOGIN_FAILED));
    }

    public void logout() {
    }

    private UserRole parseRole(String role) {
        try {
            return UserRole.valueOf(role);
        } catch (IllegalArgumentException e) {
            throw new BusinessException(ErrorCode.INVALID_MEMBER_ROLE);
        }
    }

    private Team findTeam(Long teamId) {
        return teamRepository.findByIdAndDeletedAtIsNull(teamId)
            .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_INPUT_VALUE));
    }
}
