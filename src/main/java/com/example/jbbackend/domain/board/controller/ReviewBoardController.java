package com.example.jbbackend.domain.board.controller;

import com.example.jbbackend.domain.board.dto.ReviewBoardCreateRequest;
import com.example.jbbackend.domain.board.dto.ReviewBoardResponse;
import com.example.jbbackend.domain.board.dto.ReviewStartResponse;
import com.example.jbbackend.domain.board.service.ReviewBoardService;
import com.example.jbbackend.global.Exception.ErrorCode;
import com.example.jbbackend.global.common.response.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/boards")
public class ReviewBoardController {

    private final ReviewBoardService reviewBoardService;

    @PostMapping(value = {"", "/"}, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<ReviewStartResponse>> createReviewBoard(
        @Valid @RequestBody ReviewBoardCreateRequest request
    ) {
        ReviewStartResponse response = reviewBoardService.createReviewBoard(request, List.of());
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(response, HttpStatus.CREATED));
    }

    @PostMapping(value = {"", "/"}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ReviewStartResponse>> createReviewBoardWithImage(
        @Valid @ModelAttribute ReviewBoardCreateRequest request,
        @RequestPart(required = false) List<MultipartFile> contentFiles,
        @RequestPart(required = false) MultipartFile contentFile
    ) {
        ReviewStartResponse response = reviewBoardService.createReviewBoard(
            request,
            mergeContentFiles(contentFiles, contentFile)
        );
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(response, HttpStatus.CREATED));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<ReviewBoardResponse>>> getReviewBoards() {
        return ResponseEntity.ok(ApiResponse.success(reviewBoardService.getReviewBoards()));
    }

    @PostMapping("/review/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewStartResponse>> startReview(@PathVariable Long reviewId) {
        return reviewBoardService.startReview(reviewId)
            .map(response -> ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, HttpStatus.CREATED)))
            .orElseGet(this::reviewNotFound);
    }

    private <T> ResponseEntity<ApiResponse<T>> reviewNotFound() {
        return ResponseEntity
            .status(ErrorCode.REVIEW_NOT_FOUND.getStatus())
            .body(ApiResponse.error(ErrorCode.REVIEW_NOT_FOUND));
    }

    private List<MultipartFile> mergeContentFiles(List<MultipartFile> contentFiles, MultipartFile contentFile) {
        if (contentFile == null || contentFile.isEmpty()) {
            return contentFiles == null ? List.of() : contentFiles;
        }
        if (contentFiles == null || contentFiles.isEmpty()) {
            return List.of(contentFile);
        }

        java.util.ArrayList<MultipartFile> mergedFiles = new java.util.ArrayList<>(contentFiles);
        mergedFiles.add(contentFile);
        return mergedFiles;
    }
}
