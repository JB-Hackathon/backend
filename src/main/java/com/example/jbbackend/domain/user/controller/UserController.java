package com.example.jbbackend.domain.user.controller;

import com.example.jbbackend.domain.user.dto.UserCreateRequest;
import com.example.jbbackend.domain.user.dto.UserResponse;
import com.example.jbbackend.domain.user.dto.UserUpdateRequest;
import com.example.jbbackend.domain.user.service.UserService;
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
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping({"", "/"})
    public ResponseEntity<ApiResponse<UserResponse>> createUser(
        @Valid @RequestBody UserCreateRequest request
    ) {
        UserResponse response = userService.createUser(request);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(response, HttpStatus.CREATED));
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> patchUser(
        @PathVariable Long userId,
        @Valid @RequestBody UserUpdateRequest request
    ) {
        return userService.updateUser(userId, request)
            .map(response -> ResponseEntity.ok(ApiResponse.success(response)))
            .orElseGet(this::userNotFound);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> putUser(
        @PathVariable Long userId,
        @Valid @RequestBody UserUpdateRequest request
    ) {
        return patchUser(userId, request);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long userId) {
        if (!userService.deleteUser(userId)) {
            return userNotFound();
        }
        return ResponseEntity.ok(ApiResponse.success());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> getUser(@PathVariable Long userId) {
        return userService.getUser(userId)
            .map(response -> ResponseEntity.ok(ApiResponse.success(response)))
            .orElseGet(this::userNotFound);
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getUsers() {
        return ResponseEntity.ok(ApiResponse.success(userService.getUsers()));
    }

    private <T> ResponseEntity<ApiResponse<T>> userNotFound() {
        return ResponseEntity
            .status(ErrorCode.USER_NOT_FOUND.getStatus())
            .body(ApiResponse.error(ErrorCode.USER_NOT_FOUND));
    }
}
