package com.example.jbbackend.domain.review.controller;

import com.example.jbbackend.domain.review.dto.ReviewCommentResponse;
import com.example.jbbackend.domain.review.dto.ReviewContentVersionResponse;
import com.example.jbbackend.domain.review.dto.ReviewContentVersionUpdateRequest;
import com.example.jbbackend.domain.review.dto.ReviewDetailResponse;
import com.example.jbbackend.domain.review.dto.ReviewFeedbackResponse;
import com.example.jbbackend.domain.review.dto.ReviewReportRequest;
import com.example.jbbackend.domain.review.dto.ReviewStatusUpdateRequest;
import com.example.jbbackend.domain.review.dto.ReviewSubmitRequest;
import com.example.jbbackend.domain.review.service.ReviewContentVersionService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reviews")
public class ReviewContentVersionController {

    private final ReviewContentVersionService reviewService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<ReviewContentVersionResponse>>> getReviews() {
        return ResponseEntity.ok(ApiResponse.success(reviewService.getReviews(null, null)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ReviewContentVersionResponse>>> searchReviews(
        @RequestParam(required = false) String reviewName,
        @RequestParam(required = false) String managementNumber
    ) {
        return ResponseEntity.ok(ApiResponse.success(reviewService.getReviews(reviewName, managementNumber)));
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewDetailResponse>> getReview(@PathVariable Long reviewId) {
        return reviewService.getReview(reviewId)
            .map(response -> ResponseEntity.ok(ApiResponse.success(response)))
            .orElseGet(this::reviewNotFound);
    }

    @GetMapping("/{reviewId}/comments")
    public ResponseEntity<ApiResponse<ReviewCommentResponse>> getReviewComments(@PathVariable Long reviewId) {
        return reviewService.getReviewComments(reviewId)
            .map(response -> ResponseEntity.ok(ApiResponse.success(response)))
            .orElseGet(this::reviewNotFound);
    }

    @GetMapping("/{reviewId}/feedback")
    public ResponseEntity<ApiResponse<ReviewFeedbackResponse>> getLatestReviewFeedback(@PathVariable Long reviewId) {
        return reviewService.getLatestReviewFeedback(reviewId)
            .map(response -> ResponseEntity.ok(ApiResponse.success(response)))
            .orElseGet(this::reviewNotFound);
    }

    @GetMapping("/board/{boardId}/all")
    public ResponseEntity<ApiResponse<List<ReviewContentVersionResponse>>> getReviewVersions(
        @PathVariable Long boardId
    ) {
        if (!reviewService.existsReviewBoard(boardId)) {
            return reviewNotFound();
        }
        return ResponseEntity.ok(ApiResponse.success(reviewService.getReviewVersions(boardId)));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<Void>> deleteReview(@PathVariable Long reviewId) {
        if (!reviewService.deleteReview(reviewId)) {
            return reviewNotFound();
        }
        return ResponseEntity.ok(ApiResponse.success());
    }

    @PostMapping("/{reviewId}/reports")
    public ResponseEntity<ApiResponse<ReviewContentVersionResponse>> createReport(
        @PathVariable Long reviewId,
        @Valid @RequestBody ReviewReportRequest request
    ) {
        return reviewService.updateReports(reviewId, request)
            .map(response -> ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, HttpStatus.CREATED)))
            .orElseGet(this::reviewNotFound);
    }

    @PatchMapping("/{reviewId}/edit")
    public ResponseEntity<ApiResponse<ReviewContentVersionResponse>> patchReview(
        @PathVariable Long reviewId,
        @Valid @RequestBody ReviewContentVersionUpdateRequest request
    ) {
        return reviewService.updateReview(reviewId, request)
            .map(response -> ResponseEntity.ok(ApiResponse.success(response)))
            .orElseGet(this::reviewNotFound);
    }

    @PutMapping("/{reviewId}/edit")
    public ResponseEntity<ApiResponse<ReviewContentVersionResponse>> putReview(
        @PathVariable Long reviewId,
        @Valid @RequestBody ReviewContentVersionUpdateRequest request
    ) {
        return patchReview(reviewId, request);
    }

    @PatchMapping("/{reviewId}/status")
    public ResponseEntity<ApiResponse<ReviewContentVersionResponse>> updateReviewStatus(
        @PathVariable Long reviewId,
        @Valid @RequestBody ReviewStatusUpdateRequest request
    ) {
        return reviewService.updateReviewStatus(reviewId, request)
            .map(response -> ResponseEntity.ok(ApiResponse.success(response)))
            .orElseGet(this::reviewNotFound);
    }

    @PatchMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewContentVersionResponse>> submitReview(
        @PathVariable Long reviewId,
        @Valid @RequestBody ReviewSubmitRequest request
    ) {
        return reviewService.submitReview(reviewId, request)
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
}
