package com.example.jbbackend.domain.board.entity;

import com.example.jbbackend.domain.user.entity.User;
import com.example.jbbackend.global.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "review_boards")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewBoard extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_creator_id", nullable = false)
    private User contentCreator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "compliance_advisor_id", nullable = false)
    private User complianceAdvisor;

    @Column(name = "management_number", nullable = false, unique = true, length = 50)
    private String managementNumber;

    @Column(name = "review_approval_number", unique = true, length = 50)
    private String reviewApprovalNumber;

    @Column(name = "title", nullable = false)
    private String title;

    private ReviewBoard(
        User contentCreator,
        User complianceAdvisor,
        String managementNumber,
        String reviewApprovalNumber,
        String title
    ) {
        this.contentCreator = contentCreator;
        this.complianceAdvisor = complianceAdvisor;
        this.managementNumber = managementNumber;
        this.reviewApprovalNumber = reviewApprovalNumber;
        this.title = title;
    }

    public static ReviewBoard create(
        User contentCreator,
        User complianceAdvisor,
        String managementNumber,
        String reviewApprovalNumber,
        String title
    ) {
        return new ReviewBoard(
            contentCreator,
            complianceAdvisor,
            managementNumber,
            reviewApprovalNumber,
            title
        );
    }

    public void delete() {
        softDelete();
    }

    public void updateReviewApprovalNumber(String reviewApprovalNumber) {
        this.reviewApprovalNumber = reviewApprovalNumber;
    }
}
