package com.example.jbbackend.domain.user.entity;

import com.example.jbbackend.global.common.entity.BaseTimeEntity;
import com.example.jbbackend.domain.team.entity.Team;
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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnTransformer;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String password;

    @Column(name = "username", nullable = false, length = 20)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, columnDefinition = "user_role")
    @ColumnTransformer(write = "?::user_role")
    private UserRole role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    private User(String email, String password, String name, UserRole role, Team team) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
        this.team = team;
    }

    public static User create(String email, String password, String name, UserRole role, Team team) {
        return new User(email, password, name, role, team);
    }

    public void update(String email, String password, String name, UserRole role, Team team) {
        if (email != null) {
            this.email = email;
        }
        if (password != null) {
            this.password = password;
        }
        if (name != null) {
            this.name = name;
        }
        if (role != null) {
            this.role = role;
        }
        if (team != null) {
            this.team = team;
        }
    }

    public void delete() {
        softDelete();
    }
}
