package com.epam.rd.autocode.assessment.appliances.repository;

import com.epam.rd.autocode.assessment.appliances.model.RequestAuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RequestAuditRepository extends JpaRepository<RequestAuditLog, Long> {

    // 🔍 Пошук за користувачем з сортуванням по даті (найновіші спочатку)
    List<RequestAuditLog> findByUsernameOrderByTimestampDesc(String username, Pageable pageable);

    // 📊 Останні записи загалом
    List<RequestAuditLog> findAllByOrderByTimestampDesc(Pageable pageable);

    // 📈 Кількість запитів за період
    long countByTimestampBetween(LocalDateTime startDate, LocalDateTime endDate);

    // 👤 Кількість запитів від конкретного користувача
    long countByUsername(String username);

    // 🎭 Статистика по ролях
    @Query("SELECT r.role, COUNT(r) FROM RequestAuditLog r GROUP BY r.role")
    List<Object[]> countRequestsByRole();

    // ⚠️ Підозріла активність (багато запитів з одного IP за останню годину)
    @Query("SELECT r FROM RequestAuditLog r WHERE r.timestamp >= :since " +
            "AND r.clientIp IN (SELECT r2.clientIp FROM RequestAuditLog r2 " +
            "WHERE r2.timestamp >= :since GROUP BY r2.clientIp HAVING COUNT(r2) >= :minRequests)")
    List<RequestAuditLog> findSuspiciousActivity(@Param("since") LocalDateTime since,
                                                 @Param("minRequests") int minRequests);

    // 📊 Топ користувачів за активністю
    @Query("SELECT r.username, COUNT(r) as requestCount FROM RequestAuditLog r " +
            "WHERE r.username != 'ANONYMOUS' AND r.timestamp >= :since " +
            "GROUP BY r.username ORDER BY requestCount DESC")
    List<Object[]> findTopActiveUsers(@Param("since") LocalDateTime since, Pageable pageable);

    // 🗑️ Очищення старих записів
    void deleteByTimestampBefore(LocalDateTime cutoffDate);

    // 🔧 ВИПРАВЛЕНИЙ фільтрований пошук логів з пагінацією
    @Query("SELECT r FROM RequestAuditLog r WHERE " +
            "(:username IS NULL OR :username = '' OR LOWER(r.username) LIKE LOWER(CONCAT('%', :username, '%'))) AND " +
            "(:method IS NULL OR :method = '' OR r.method = :method) AND " +
            "(:role IS NULL OR :role = '' OR r.role = :role) " +
            "ORDER BY r.timestamp DESC")
    Page<RequestAuditLog> findFilteredLogs(@Param("username") String username,
                                           @Param("method") String method,
                                           @Param("role") String role,
                                           Pageable pageable);

    // 📈 Кількість унікальних користувачів
    @Query("SELECT COUNT(DISTINCT r.username) FROM RequestAuditLog r " +
            "WHERE r.username NOT IN ('ANONYMOUS', 'INVALID_TOKEN')")
    long countDistinctUsername();

    // 🌍 Кількість унікальних IP адрес
    @Query("SELECT COUNT(DISTINCT r.clientIp) FROM RequestAuditLog r")
    long countDistinctClientIp();

    // 📅 Кількість логів після певної дати
    long countByTimestampAfter(LocalDateTime timestamp);

    // 🌐 Статистика по IP адресах
    @Query("SELECT r.clientIp, COUNT(r) as requestCount FROM RequestAuditLog r " +
            "WHERE r.timestamp >= :since AND r.clientIp IS NOT NULL " +
            "GROUP BY r.clientIp ORDER BY requestCount DESC")
    List<Object[]> findIpStats(@Param("since") LocalDateTime since, Pageable pageable);

    // 🔍 Альтернативні методи для окремих фільтрів (якщо основний не працює)

    // Пошук тільки по username
    Page<RequestAuditLog> findByUsernameContainingIgnoreCaseOrderByTimestampDesc(String username, Pageable pageable);

    // Пошук тільки по method
    Page<RequestAuditLog> findByMethodOrderByTimestampDesc(String method, Pageable pageable);

    // Пошук тільки по role
    Page<RequestAuditLog> findByRoleOrderByTimestampDesc(String role, Pageable pageable);

    // Комбіновані пошуки
    Page<RequestAuditLog> findByUsernameContainingIgnoreCaseAndMethodOrderByTimestampDesc(String username, String method, Pageable pageable);

    Page<RequestAuditLog> findByUsernameContainingIgnoreCaseAndRoleOrderByTimestampDesc(String username, String role, Pageable pageable);

    Page<RequestAuditLog> findByMethodAndRoleOrderByTimestampDesc(String method, String role, Pageable pageable);

    Page<RequestAuditLog> findByUsernameContainingIgnoreCaseAndMethodAndRoleOrderByTimestampDesc(String username, String method, String role, Pageable pageable);
}