package com.example.mymoo.domain.account.entity;

import com.example.mymoo.global.entity.BaseEntity;
import com.example.mymoo.global.enums.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "TB_ACCOUNT")
@Entity
public class Account extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String email;

    @NotNull
    @Column(nullable = false)
    private String password;

    @Size(min = 10, max = 11)
    @Column(nullable = true) // kakao 로그인 시 못넣음
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true) // kakao 로그인 시 못넣음
    private UserRole role;

    @Builder
    public Account(
        final String email,
        final String password,
        final String phoneNumber,
        final UserRole role
    ) {
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }
}