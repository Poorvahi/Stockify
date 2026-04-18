package org.example.stockifyims.repository.auth;

import org.example.stockifyims.entity.PasswordResetOtpVo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetOtpRepository extends JpaRepository<PasswordResetOtpVo, Long> {
    Optional<PasswordResetOtpVo> findTopByEmailOrderByCreatedAtDesc(String email);
}
