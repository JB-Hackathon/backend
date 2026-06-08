package com.example.jbbackend.domain.review.repository;

import com.example.jbbackend.domain.review.entity.ReviewContentVersion;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewContentVersionRepository extends JpaRepository<ReviewContentVersion, Long> {

    Optional<ReviewContentVersion> findByIdAndDeletedAtIsNull(Long id);

    List<ReviewContentVersion> findAllByDeletedAtIsNullOrderByIdAsc();

    List<ReviewContentVersion> findAllByBoard_IdAndDeletedAtIsNullOrderByVersionNoAsc(Long boardId);

    @Query("""
        select review
        from ReviewContentVersion review
        where review.board.id in :boardIds
          and review.deletedAt is null
        order by review.board.id asc, review.versionNo desc
        """)
    List<ReviewContentVersion> findLatestCandidatesByBoardIds(@Param("boardIds") List<Long> boardIds);

    Optional<ReviewContentVersion> findFirstByBoard_IdAndDeletedAtIsNullOrderByVersionNoDesc(Long boardId);
}
