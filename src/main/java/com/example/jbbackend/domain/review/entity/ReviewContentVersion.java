package com.example.jbbackend.domain.review.entity;

import com.example.jbbackend.domain.board.entity.ReviewBoard;
import com.example.jbbackend.global.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnTransformer;

@Getter
@Entity
@Table(
    name = "review_content_versions",
    uniqueConstraints = @UniqueConstraint(columnNames = {"board_id", "version_no"})
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewContentVersion extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "content_version_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private ReviewBoard board;

    @Column(name = "version_no", nullable = false)
    private Integer versionNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "business_sector", nullable = false, columnDefinition = "business_sector")
    @ColumnTransformer(write = "?::business_sector")
    private BusinessSector businessSector;

    @Enumerated(EnumType.STRING)
    @Column(name = "channel_type", nullable = false, columnDefinition = "channel_type")
    @ColumnTransformer(write = "?::channel_type")
    private ChannelType channelType;

    @Enumerated(EnumType.STRING)
    @Column(name = "content_type", nullable = false, columnDefinition = "content_type")
    @ColumnTransformer(write = "?::content_type")
    private ContentType contentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "content_category", nullable = false, columnDefinition = "content_category")
    @ColumnTransformer(write = "?::content_category")
    private ContentCategory contentCategory;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_category", columnDefinition = "product_category")
    @ColumnTransformer(write = "?::product_category")
    private ProductCategory productCategory;

    @Enumerated(EnumType.STRING)
    @Column(name = "language_code", nullable = false, columnDefinition = "language_code")
    @ColumnTransformer(write = "?::language_code")
    private LanguageCode languageCode;

    @Column(name = "content_file_path", columnDefinition = "TEXT")
    private String contentFilePath;

    @Column(name = "content_text", columnDefinition = "TEXT")
    private String contentText;

    @Column(name = "content_description")
    private String contentDescription;

    @Enumerated(EnumType.STRING)
    @Column(name = "review_status", nullable = false, columnDefinition = "review_status")
    @ColumnTransformer(write = "?::review_status")
    private ReviewStatus reviewStatus;

    @Column(name = "review_comments", columnDefinition = "TEXT")
    private String reviewComments;

    @Column(name = "review_reports", columnDefinition = "TEXT")
    private String reviewReports;

    private ReviewContentVersion(
        ReviewBoard board,
        Integer versionNo,
        BusinessSector businessSector,
        ChannelType channelType,
        ContentType contentType,
        ContentCategory contentCategory,
        ProductCategory productCategory,
        LanguageCode languageCode,
        String contentFilePath,
        String contentText,
        String contentDescription,
        ReviewStatus reviewStatus,
        String reviewComments,
        String reviewReports
    ) {
        this.board = board;
        this.versionNo = versionNo;
        this.businessSector = businessSector;
        this.channelType = channelType;
        this.contentType = contentType;
        this.contentCategory = contentCategory;
        this.productCategory = productCategory;
        this.languageCode = languageCode;
        this.contentFilePath = contentFilePath;
        this.contentText = contentText;
        this.contentDescription = contentDescription;
        this.reviewStatus = reviewStatus;
        this.reviewComments = reviewComments;
        this.reviewReports = reviewReports;
    }

    public static ReviewContentVersion create(
        ReviewBoard board,
        Integer versionNo,
        BusinessSector businessSector,
        ChannelType channelType,
        ContentType contentType,
        ContentCategory contentCategory,
        ProductCategory productCategory,
        LanguageCode languageCode,
        String contentFilePath,
        String contentText,
        String contentDescription,
        ReviewStatus reviewStatus,
        String reviewComments,
        String reviewReports
    ) {
        return new ReviewContentVersion(
            board,
            versionNo,
            businessSector,
            channelType,
            contentType,
            contentCategory,
            productCategory,
            languageCode,
            contentFilePath,
            contentText,
            contentDescription,
            reviewStatus == null ? ReviewStatus.pending : reviewStatus,
            reviewComments,
            reviewReports
        );
    }

    public void update(
        BusinessSector businessSector,
        ChannelType channelType,
        ContentType contentType,
        ContentCategory contentCategory,
        ProductCategory productCategory,
        LanguageCode languageCode,
        String contentFilePath,
        String contentText,
        String contentDescription,
        ReviewStatus reviewStatus,
        String reviewComments,
        String reviewReports
    ) {
        if (businessSector != null) {
            this.businessSector = businessSector;
        }
        if (channelType != null) {
            this.channelType = channelType;
        }
        if (contentType != null) {
            this.contentType = contentType;
        }
        if (contentCategory != null) {
            this.contentCategory = contentCategory;
        }
        if (productCategory != null) {
            this.productCategory = productCategory;
        }
        if (languageCode != null) {
            this.languageCode = languageCode;
        }
        if (contentFilePath != null) {
            this.contentFilePath = contentFilePath;
        }
        if (contentText != null) {
            this.contentText = contentText;
        }
        if (contentDescription != null) {
            this.contentDescription = contentDescription;
        }
        if (reviewStatus != null) {
            this.reviewStatus = reviewStatus;
        }
        if (reviewComments != null) {
            this.reviewComments = reviewComments;
        }
        if (reviewReports != null) {
            this.reviewReports = reviewReports;
        }
    }

    public void submit() {
        this.reviewStatus = ReviewStatus.pending;
    }

    public void updateReports(String reviewReports) {
        this.reviewReports = reviewReports;
    }

    public void delete() {
        softDelete();
    }
}
