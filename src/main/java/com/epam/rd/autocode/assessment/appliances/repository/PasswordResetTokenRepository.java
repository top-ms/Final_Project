package com.epam.rd.autocode.assessment.appliances.repository;

import com.epam.rd.autocode.assessment.appliances.model.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    /**
     * Знайти токен по його значенню
     */
    Optional<PasswordResetToken> findByToken(String token);

    /**
     * Знайти активні токени для email
     */
    @Query("SELECT p FROM PasswordResetToken p WHERE p.email = :email AND p.isUsed = false AND p.expiryDate > :now")
    Optional<PasswordResetToken> findActiveTokenByEmail(@Param("email") String email, @Param("now") LocalDateTime now);

    /**
     * Деактивувати всі токени для email
     */
    @Modifying
    @Query("UPDATE PasswordResetToken p SET p.isUsed = true WHERE p.email = :email AND p.isUsed = false")
    void deactivateAllTokensForEmail(@Param("email") String email);

    /**
     * Видалити застарілі токени
     */
    @Modifying
    @Query("DELETE FROM PasswordResetToken p WHERE p.expiryDate < :expiryDate")
    void deleteExpiredTokens(@Param("expiryDate") LocalDateTime expiryDate);

    /**
     * Перевірити чи існує активний токен для email
     */
    @Query("SELECT COUNT(p) > 0 FROM PasswordResetToken p WHERE p.email = :email AND p.isUsed = false AND p.expiryDate > :now")
    boolean existsActiveTokenByEmail(@Param("email") String email, @Param("now") LocalDateTime now);
}