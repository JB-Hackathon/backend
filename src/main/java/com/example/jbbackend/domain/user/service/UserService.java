package com.example.jbbackend.domain.user.service;

import com.example.jbbackend.domain.user.dto.UserCreateRequest;
import com.example.jbbackend.domain.user.dto.UserResponse;
import com.example.jbbackend.domain.user.dto.UserUpdateRequest;
import com.example.jbbackend.domain.user.entity.User;
import com.example.jbbackend.domain.user.entity.UserRole;
import com.example.jbbackend.domain.user.repository.UserRepository;
import com.example.jbbackend.global.Exception.BusinessException;
import com.example.jbbackend.global.Exception.ErrorCode;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserResponse createUser(UserCreateRequest request) {
        validateEmailNotDuplicated(request.email());
        User user = User.create(
            request.email(),
            request.password(),
            request.name(),
            parseRole(request.role()),
            request.teamId()
        );
        return UserResponse.from(userRepository.save(user));
    }

    @Transactional
    public Optional<UserResponse> updateUser(Long userId, UserUpdateRequest request) {
        return userRepository.findByIdAndDeletedAtIsNull(userId)
            .map(user -> {
                if (request.email() != null && !request.email().equals(user.getEmail())) {
                    validateEmailNotDuplicated(request.email());
                }
                user.update(
                    request.email(),
                    request.password(),
                    request.name(),
                    parseNullableRole(request.role()),
                    request.teamId()
                );
                return UserResponse.from(user);
            });
    }

    @Transactional
    public boolean deleteUser(Long userId) {
        return userRepository.findByIdAndDeletedAtIsNull(userId)
            .map(user -> {
                user.delete();
                return true;
            })
            .orElse(false);
    }

    public Optional<UserResponse> getUser(Long userId) {
        return userRepository.findByIdAndDeletedAtIsNull(userId)
            .map(UserResponse::from);
    }

    public List<UserResponse> getUsers() {
        return userRepository.findAllByDeletedAtIsNullOrderByIdAsc()
            .stream()
            .map(UserResponse::from)
            .toList();
    }

    private void validateEmailNotDuplicated(String email) {
        if (userRepository.existsByEmailAndDeletedAtIsNull(email)) {
            throw new BusinessException(ErrorCode.EMAIL_DUPLICATION);
        }
    }

    private UserRole parseRole(String role) {
        try {
            return UserRole.valueOf(role);
        } catch (IllegalArgumentException e) {
            throw new BusinessException(ErrorCode.INVALID_MEMBER_ROLE);
        }
    }

    private UserRole parseNullableRole(String role) {
        if (role == null) {
            return null;
        }
        return parseRole(role);
    }
}
