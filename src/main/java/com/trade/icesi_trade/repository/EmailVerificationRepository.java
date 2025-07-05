package com.trade.icesi_trade.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.trade.icesi_trade.model.EmailVerification;
import com.trade.icesi_trade.model.EmailVerification.VerificationType;

@Repository
public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {

    Optional<EmailVerification> findByToken(String token);

    Optional<EmailVerification> findByEmailAndTypeAndUsedFalse(String email, VerificationType type);

    @Query("SELECT ev FROM EmailVerification ev WHERE ev.email = :email AND ev.type = :type AND ev.used = false AND ev.expiresAt > :now")
    Optional<EmailVerification> findValidVerification(@Param("email") String email,
            @Param("type") VerificationType type,
            @Param("now") LocalDateTime now);

    void deleteByEmailAndType(String email, VerificationType type);

    @Query("SELECT COUNT(ev) > 0 FROM EmailVerification ev WHERE ev.email = :email AND ev.type = :type AND ev.used = false AND ev.expiresAt > :now")
    boolean existsValidVerification(@Param("email") String email,
            @Param("type") VerificationType type,
            @Param("now") LocalDateTime now);
}