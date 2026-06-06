package com.example.jbbackend.domain.board.repository;

import com.example.jbbackend.domain.board.entity.ReviewBoard;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewBoardRepository extends JpaRepository<ReviewBoard, Long> {

    boolean existsByManagementNumberAndDeletedAtIsNull(String managementNumber);

    Optional<ReviewBoard> findByIdAndDeletedAtIsNull(Long id);

    List<ReviewBoard> findAllByDeletedAtIsNullOrderByIdAsc();

    List<ReviewBoard> findAllByTitleContainingIgnoreCaseAndDeletedAtIsNullOrderByIdAsc(String title);

    List<ReviewBoard> findAllByManagementNumberContainingIgnoreCaseAndDeletedAtIsNullOrderByIdAsc(
        String managementNumber
    );
}
