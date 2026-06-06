package com.example.jbbackend.domain.review.service;

import com.example.jbbackend.domain.board.entity.ReviewBoard;
import com.example.jbbackend.domain.board.repository.ReviewBoardRepository;
import com.example.jbbackend.domain.review.dto.ReviewContentVersionResponse;
import com.example.jbbackend.domain.review.dto.ReviewContentVersionUpdateRequest;
import com.example.jbbackend.domain.review.dto.ReviewReportRequest;
import com.example.jbbackend.domain.review.dto.ReviewSubmitRequest;
import com.example.jbbackend.domain.review.entity.BusinessSector;
import com.example.jbbackend.domain.review.entity.ChannelType;
import com.example.jbbackend.domain.review.entity.ContentCategory;
import com.example.jbbackend.domain.review.entity.ContentType;
import com.example.jbbackend.domain.review.entity.LanguageCode;
import com.example.jbbackend.domain.review.entity.ProductCategory;
import com.example.jbbackend.domain.review.entity.ReviewContentVersion;
import com.example.jbbackend.domain.review.entity.ReviewStatus;
import com.example.jbbackend.domain.review.repository.ReviewContentVersionRepository;
import com.example.jbbackend.global.Exception.BusinessException;
import com.example.jbbackend.global.Exception.ErrorCode;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewContentVersionService {

    private final ReviewBoardRepository reviewBoardRepository;
    private final ReviewContentVersionRepository reviewRepository;

    @Transactional
    public Optional<ReviewContentVersionResponse> updateReview(
        Long boardId,
        ReviewContentVersionUpdateRequest request
    ) {
        return findLatestReviewVersion(boardId)
            .map(review -> {
                review.update(
                    parseNullableBusinessSector(request.businessSector()),
                    parseNullableChannelType(request.channelType()),
                    parseNullableContentType(request.contentType()),
                    parseNullableContentCategory(request.contentCategory()),
                    parseNullableProductCategory(request.productCategory()),
                    parseNullableLanguageCode(request.languageCode()),
                    request.contentFilePath(),
                    request.contentText(),
                    request.contentDescription(),
                    parseNullableReviewStatus(request.reviewStatus()),
                    request.reviewComments(),
                    request.reviewReports()
                );
                return ReviewContentVersionResponse.from(review);
            });
    }

    @Transactional
    public Optional<ReviewContentVersionResponse> updateReports(Long boardId, ReviewReportRequest request) {
        return findLatestReviewVersion(boardId)
            .map(review -> {
                review.updateReports(request.reviewReports());
                return ReviewContentVersionResponse.from(review);
            });
    }

    @Transactional
    public Optional<ReviewContentVersionResponse> submitReview(Long boardId, ReviewSubmitRequest request) {
        return findLatestReviewVersion(boardId)
            .map(review -> {
                review.update(
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    request.reviewStatus() == null
                        ? ReviewStatus.approved
                        : parseNullableReviewStatus(request.reviewStatus()),
                    request.reviewComments(),
                    request.reviewReports()
                );
                return ReviewContentVersionResponse.from(review);
            });
    }

    @Transactional
    public boolean deleteReview(Long boardId) {
        return reviewBoardRepository.findByIdAndDeletedAtIsNull(boardId)
            .map(board -> {
                board.delete();
                reviewRepository.findAllByBoardIdAndDeletedAtIsNullOrderByVersionNoAsc(boardId)
                    .forEach(ReviewContentVersion::delete);
                return true;
            })
            .orElse(false);
    }

    public Optional<ReviewContentVersionResponse> getReview(Long reviewId) {
        return reviewRepository.findByIdAndDeletedAtIsNull(reviewId)
            .map(ReviewContentVersionResponse::from);
    }

    public List<ReviewContentVersionResponse> getReviews(String reviewName, String managementNumber) {
        if (reviewName != null && !reviewName.isBlank()) {
            return getLatestVersions(
                reviewBoardRepository.findAllByTitleContainingIgnoreCaseAndDeletedAtIsNullOrderByIdAsc(reviewName)
            );
        }
        if (managementNumber != null && !managementNumber.isBlank()) {
            return getLatestVersions(
                reviewBoardRepository
                    .findAllByManagementNumberContainingIgnoreCaseAndDeletedAtIsNullOrderByIdAsc(managementNumber)
            );
        }
        return getLatestVersions(reviewBoardRepository.findAllByDeletedAtIsNullOrderByIdAsc());
    }

    public List<ReviewContentVersionResponse> getReviewVersions(Long boardId) {
        return reviewRepository.findAllByBoardIdAndDeletedAtIsNullOrderByVersionNoAsc(boardId)
            .stream()
            .map(ReviewContentVersionResponse::from)
            .toList();
    }

    public boolean existsReviewBoard(Long boardId) {
        return reviewBoardRepository.findByIdAndDeletedAtIsNull(boardId).isPresent();
    }

    private Optional<ReviewContentVersion> findLatestReviewVersion(Long boardId) {
        if (reviewBoardRepository.findByIdAndDeletedAtIsNull(boardId).isEmpty()) {
            return Optional.empty();
        }
        return reviewRepository.findFirstByBoardIdAndDeletedAtIsNullOrderByVersionNoDesc(boardId);
    }

    private List<ReviewContentVersionResponse> getLatestVersions(List<ReviewBoard> boards) {
        List<Long> boardIds = boards.stream()
            .map(ReviewBoard::getId)
            .toList();

        if (boardIds.isEmpty()) {
            return List.of();
        }

        Map<Long, ReviewContentVersion> latestVersions = new LinkedHashMap<>();
        reviewRepository.findAllByBoardIdInAndDeletedAtIsNullOrderByBoardIdAscVersionNoDesc(boardIds)
            .forEach(review -> latestVersions.putIfAbsent(review.getBoardId(), review));

        return latestVersions.values()
            .stream()
            .map(ReviewContentVersionResponse::from)
            .toList();
    }

    private BusinessSector parseBusinessSector(String value) {
        return parseEnum(BusinessSector.class, value);
    }

    private BusinessSector parseNullableBusinessSector(String value) {
        return value == null ? null : parseBusinessSector(value);
    }

    private ChannelType parseChannelType(String value) {
        return parseEnum(ChannelType.class, value);
    }

    private ChannelType parseNullableChannelType(String value) {
        return value == null ? null : parseChannelType(value);
    }

    private ContentType parseContentType(String value) {
        if ("image".equals(value)) {
            return ContentType.file;
        }
        if ("text_image".equals(value) || "text+image".equals(value)) {
            return ContentType.file_with_text;
        }
        return parseEnum(ContentType.class, value);
    }

    private ContentType parseNullableContentType(String value) {
        return value == null ? null : parseContentType(value);
    }

    private ContentCategory parseContentCategory(String value) {
        return parseEnum(ContentCategory.class, value);
    }

    private ContentCategory parseNullableContentCategory(String value) {
        return value == null ? null : parseContentCategory(value);
    }

    private ProductCategory parseNullableProductCategory(String value) {
        return value == null ? null : parseEnum(ProductCategory.class, value);
    }

    private LanguageCode parseLanguageCode(String value) {
        return parseEnum(LanguageCode.class, value);
    }

    private LanguageCode parseNullableLanguageCode(String value) {
        return value == null ? null : parseLanguageCode(value);
    }

    private ReviewStatus parseNullableReviewStatus(String value) {
        return value == null ? null : parseEnum(ReviewStatus.class, value);
    }

    private <T extends Enum<T>> T parseEnum(Class<T> enumType, String value) {
        try {
            return Enum.valueOf(enumType, value);
        } catch (IllegalArgumentException e) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
        }
    }
}
