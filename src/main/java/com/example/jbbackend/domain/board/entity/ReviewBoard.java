package com.example.jbbackend.domain.board.entity;

import com.example.jbbackend.global.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    @Column(name = "content_creator_id", nullable = false)
    private Long contentCreatorId;

    @Column(name = "compliance_advisor_id", nullable = false)
    private Long complianceAdvisorId;

    @Column(name = "management_number", nullable = false, unique = true, length = 50)
    private String managementNumber;

    @Column(name = "review_approval_number", unique = true, length = 50)
    private String reviewApprovalNumber;

    @Column(name = "title", nullable = false)
    private String title;

    private ReviewBoard(
        Long contentCreatorId,
        Long complianceAdvisorId,
        String managementNumber,
        String reviewApprovalNumber,
        String title
    ) {
        this.contentCreatorId = contentCreatorId;
        this.complianceAdvisorId = complianceAdvisorId;
        this.managementNumber = managementNumber;
        this.reviewApprovalNumber = reviewApprovalNumber;
        this.title = title;
    }

    public static ReviewBoard create(
        Long contentCreatorId,
        Long complianceAdvisorId,
        String managementNumber,
        String reviewApprovalNumber,
        String title
    ) {
        return new ReviewBoard(
            contentCreatorId,
            complianceAdvisorId,
            managementNumber,
            reviewApprovalNumber,
            title
        );
    }

    public void delete() {
        softDelete();
    }
}
