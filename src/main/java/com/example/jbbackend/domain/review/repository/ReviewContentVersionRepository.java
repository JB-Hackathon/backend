package com.example.jbbackend.domain.review.repository;

import com.example.jbbackend.domain.review.entity.ReviewContentVersion;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewContentVersionRepository extends JpaRepository<ReviewContentVersion, Long> {

    Optional<ReviewContentVersion> findByIdAndDeletedAtIsNull(Long id);

    List<ReviewContentVersion> findAllByDeletedAtIsNullOrderByIdAsc();

    List<ReviewContentVersion> findAllByBoardIdAndDeletedAtIsNullOrderByVersionNoAsc(Long boardId);

    List<ReviewContentVersion> findAllByBoardIdInAndDeletedAtIsNullOrderByBoardIdAscVersionNoDesc(List<Long> boardIds);

    Optional<ReviewContentVersion> findFirstByBoardIdAndDeletedAtIsNullOrderByVersionNoDesc(Long boardId);
}
