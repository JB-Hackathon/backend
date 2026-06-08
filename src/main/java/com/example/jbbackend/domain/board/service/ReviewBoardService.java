package com.example.jbbackend.domain.board.service;

import com.example.jbbackend.domain.board.dto.ReviewBoardCreateRequest;
import com.example.jbbackend.domain.board.dto.ReviewBoardResponse;
import com.example.jbbackend.domain.board.dto.ReviewStartResponse;
import com.example.jbbackend.domain.board.entity.ReviewBoard;
import com.example.jbbackend.domain.board.repository.ReviewBoardRepository;
import com.example.jbbackend.domain.review.dto.ReviewContentVersionResponse;
import com.example.jbbackend.domain.review.entity.BusinessSector;
import com.example.jbbackend.domain.review.entity.ChannelType;
import com.example.jbbackend.domain.review.entity.ContentCategory;
import com.example.jbbackend.domain.review.entity.ContentType;
import com.example.jbbackend.domain.review.entity.LanguageCode;
import com.example.jbbackend.domain.review.entity.ProductCategory;
import com.example.jbbackend.domain.review.entity.ReviewContentVersion;
import com.example.jbbackend.domain.review.entity.ReviewStatus;
import com.example.jbbackend.domain.review.repository.ReviewContentVersionRepository;
import com.example.jbbackend.domain.user.entity.User;
import com.example.jbbackend.domain.user.repository.UserRepository;
import com.example.jbbackend.global.Exception.BusinessException;
import com.example.jbbackend.global.Exception.ErrorCode;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewBoardService {

    private final ReviewBoardRepository reviewBoardRepository;
    private final ReviewContentVersionRepository reviewContentVersionRepository;
    private final UserRepository userRepository;

    @Transactional
    public ReviewStartResponse createReviewBoard(ReviewBoardCreateRequest request, List<MultipartFile> contentFiles) {
        if (reviewBoardRepository.existsByManagementNumberAndDeletedAtIsNull(request.managementNumber())) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "이미 사용 중인 관리번호입니다.");
        }

        String storedFilePath = saveContentFiles(contentFiles).orElse(request.contentFilePath());
        ContentType contentType = parseContentType(request.contentType());
        validateContent(contentType, request.contentText(), storedFilePath);

        User contentCreator = findUser(request.contentCreatorId());
        User complianceAdvisor = findUser(request.complianceAdvisorId());

        ReviewBoard board = ReviewBoard.create(
            contentCreator,
            complianceAdvisor,
            request.managementNumber(),
            request.reviewApprovalNumber(),
            request.title()
        );
        ReviewBoard savedBoard = reviewBoardRepository.save(board);

        ReviewContentVersion initialVersion = ReviewContentVersion.create(
            savedBoard,
            1,
            parseBusinessSector(request.businessSector()),
            parseChannelType(request.channelType()),
            contentType,
            parseContentCategory(request.contentCategory()),
            parseNullableProductCategory(request.productCategory()),
            parseLanguageCode(request.languageCode()),
            storedFilePath,
            request.contentText(),
            request.contentDescription(),
            ReviewStatus.pending,
            null,
            null
        );
        ReviewContentVersion savedVersion = reviewContentVersionRepository.save(initialVersion);

        return new ReviewStartResponse(
            ReviewBoardResponse.from(savedBoard, savedVersion),
            ReviewContentVersionResponse.from(savedVersion)
        );
    }

    public List<ReviewBoardResponse> getReviewBoards() {
        List<ReviewBoard> boards = reviewBoardRepository.findAllByDeletedAtIsNullOrderByIdAsc();
        List<Long> boardIds = boards.stream()
            .map(ReviewBoard::getId)
            .toList();

        if (boardIds.isEmpty()) {
            return List.of();
        }

        Map<Long, ReviewContentVersion> latestVersions = reviewContentVersionRepository
            .findLatestCandidatesByBoardIds(boardIds)
            .stream()
            .collect(Collectors.toMap(
                review -> review.getBoard().getId(),
                Function.identity(),
                (existing, ignored) -> existing
            ));

        return boards
            .stream()
            .map(board -> ReviewBoardResponse.from(board, latestVersions.get(board.getId())))
            .toList();
    }

    public Optional<ReviewStartResponse> startReview(Long reviewId) {
        return reviewBoardRepository.findByIdAndDeletedAtIsNull(reviewId)
            .map(board -> {
                ReviewContentVersion latestVersion = reviewContentVersionRepository
                    .findFirstByBoard_IdAndDeletedAtIsNullOrderByVersionNoDesc(reviewId)
                    .orElse(null);
                return new ReviewStartResponse(
                    ReviewBoardResponse.from(board, latestVersion),
                    latestVersion == null ? null : ReviewContentVersionResponse.from(latestVersion)
                );
            });
    }

    private void validateContent(ContentType contentType, String contentText, String contentFilePath) {
        boolean hasText = contentText != null && !contentText.isBlank();
        boolean hasFile = contentFilePath != null && !contentFilePath.isBlank();

        if (contentType == ContentType.text && !hasText) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "텍스트 심의는 contentText가 필요합니다.");
        }
        if (contentType == ContentType.file && !hasFile) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "이미지 심의는 contentFile 또는 contentFilePath가 필요합니다.");
        }
        if (contentType == ContentType.file_with_text && (!hasText || !hasFile)) {
            throw new BusinessException(
                ErrorCode.INVALID_INPUT_VALUE,
                "텍스트+이미지 심의는 contentText와 contentFile 또는 contentFilePath가 모두 필요합니다."
            );
        }
    }

    private Optional<String> saveContentFiles(List<MultipartFile> contentFiles) {
        if (contentFiles == null || contentFiles.isEmpty()) {
            return Optional.empty();
        }

        try {
            Path uploadDirectory = Path.of("uploads", "reviews");
            Files.createDirectories(uploadDirectory);

            List<String> storedFilePaths = contentFiles.stream()
                .filter(contentFile -> contentFile != null && !contentFile.isEmpty())
                .map(contentFile -> saveContentFile(uploadDirectory, contentFile))
                .toList();

            if (storedFilePaths.isEmpty()) {
                return Optional.empty();
            }

            return Optional.of(String.join(",", storedFilePaths));
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "파일 저장 중 오류가 발생했습니다.");
        }
    }

    private String saveContentFile(Path uploadDirectory, MultipartFile contentFile) {
        try {
            String storedFilename = UUID.randomUUID() + getExtension(contentFile.getOriginalFilename());
            Path targetPath = uploadDirectory.resolve(storedFilename);
            Files.copy(contentFile.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            return targetPath.toString().replace("\\", "/");
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "파일 저장 중 오류가 발생했습니다.");
        }
    }

    private User findUser(Long userId) {
        return userRepository.findByIdAndDeletedAtIsNull(userId)
            .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_INPUT_VALUE));
    }

    private String getExtension(String filename) {
        if (filename == null || filename.isBlank()) {
            return "";
        }

        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex < 0) {
            return "";
        }
        return filename.substring(dotIndex);
    }

    private BusinessSector parseBusinessSector(String value) {
        return parseEnum(BusinessSector.class, value);
    }

    private ChannelType parseChannelType(String value) {
        return parseEnum(ChannelType.class, value);
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

    private ContentCategory parseContentCategory(String value) {
        return parseEnum(ContentCategory.class, value);
    }

    private ProductCategory parseNullableProductCategory(String value) {
        return value == null ? null : parseEnum(ProductCategory.class, value);
    }

    private LanguageCode parseLanguageCode(String value) {
        return parseEnum(LanguageCode.class, value);
    }

    private <T extends Enum<T>> T parseEnum(Class<T> enumType, String value) {
        try {
            return Enum.valueOf(enumType, value);
        } catch (IllegalArgumentException e) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
        }
    }
}
