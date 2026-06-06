package com.example.jbbackend.domain.team.repository;

import com.example.jbbackend.domain.team.entity.Team;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {

    Optional<Team> findByIdAndDeletedAtIsNull(Long id);

    List<Team> findAllByDeletedAtIsNullOrderByIdAsc();
}
