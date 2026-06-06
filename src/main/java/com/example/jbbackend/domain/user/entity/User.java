package com.example.jbbackend.domain.user.entity;

import com.example.jbbackend.global.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    @Column(name = "team_id", nullable = false)
    private Long teamId;

    private User(String email, String password, String name, UserRole role, Long teamId) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
        this.teamId = teamId;
    }

    public static User create(String email, String password, String name, UserRole role, Long teamId) {
        return new User(email, password, name, role, teamId);
    }

    public void update(String email, String password, String name, UserRole role, Long teamId) {
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
        if (teamId != null) {
            this.teamId = teamId;
        }
    }

    public void delete() {
        softDelete();
    }
}
