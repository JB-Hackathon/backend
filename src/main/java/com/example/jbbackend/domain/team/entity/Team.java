package com.example.jbbackend.domain.team.entity;

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
@Table(name = "teams")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long id;

    @Column(name = "name", nullable = false, length = 20)
    private String name;

    private Team(String name) {
        this.name = name;
    }

    public static Team create(String name) {
        return new Team(name);
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void delete() {
        softDelete();
    }
}
