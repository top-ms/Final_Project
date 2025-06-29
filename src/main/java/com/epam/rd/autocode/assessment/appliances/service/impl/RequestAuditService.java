package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.model.RequestAuditLog;
import com.epam.rd.autocode.assessment.appliances.repository.RequestAuditRepository;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RequestAuditService {

    private static final Logger log = LoggerFactory.getLogger(RequestAuditService.class);

    private final RequestAuditRepository auditRepository;

    public RequestAuditService(RequestAuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    // 🚀 Асинхронне логування запитів
    @Async
    public void logRequestAsync(String username, String userId, String role,
                                String path, String method, String clientIp, String userAgent) {
        try {
            RequestAuditLog auditLog = RequestAuditLog.builder()
                    .username(username)
                    .userId(userId)
                    .role(role)
                    .path(path)
                    .method(method)
                    .clientIp(clientIp)
                    .userAgent(userAgent)
                    .timestamp(LocalDateTime.now())
                    .build();

            auditRepository.save(auditLog);

            log.debug("✅ Audit log saved for user: {} on path: {}", username, path);

        } catch (Exception e) {
            log.error("❌ Failed to save audit log for user: {} on path: {}", username, path, e);
        }
    }

    // 📊 Синхронне логування (якщо потрібно)
    public void logRequest(String username, String userId, String role,
                           String path, String method, String clientIp, String userAgent) {
        try {
            RequestAuditLog auditLog = RequestAuditLog.builder()
                    .username(username)
                    .userId(userId)
                    .role(role)
                    .path(path)
                    .method(method)
                    .clientIp(clientIp)
                    .userAgent(userAgent)
                    .timestamp(LocalDateTime.now())
                    .build();

            auditRepository.save(auditLog);

        } catch (Exception e) {
            log.error("❌ Failed to save audit log", e);
        }
    }

    // 🔍 Методи для отримання статистики
    public List<RequestAuditLog> getRecentActivityByUser(String username, int limit) {
        return auditRepository.findByUsernameOrderByTimestampDesc(username, PageRequest.of(0, limit));
    }

    public List<RequestAuditLog> getRecentActivity(int limit) {
        return auditRepository.findAllByOrderByTimestampDesc(PageRequest.of(0, limit));
    }

    public long getTotalRequestsToday() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        return auditRepository.countByTimestampBetween(startOfDay, endOfDay);
    }

    public long getTotalRequestsByUser(String username) {
        return auditRepository.countByUsername(username);
    }

    // 📈 Статистика по ролях
    public Map<String, Long> getRequestCountByRole() {
        List<Object[]> results = auditRepository.countRequestsByRole();
        return results.stream()
                .collect(Collectors.toMap(
                        result -> (String) result[0],
                        result -> (Long) result[1]
                ));
    }

    // 🌍 Статистика по IP адресах
    public List<RequestAuditLog> getSuspiciousActivity(int minRequestsPerHour) {
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        return auditRepository.findSuspiciousActivity(oneHourAgo, minRequestsPerHour);
    }



    public Page<RequestAuditLog> getFilteredLogs(int page, int size, String username, String method, String role) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());

        // Нормалізуємо параметри (перетворюємо порожні рядки в null)
        username = (username != null && username.trim().isEmpty()) ? null : username;
        method = (method != null && method.trim().isEmpty()) ? null : method;
        role = (role != null && role.trim().isEmpty()) ? null : role;

        System.out.println("🔍 Filtering logs with: username=" + username + ", method=" + method + ", role=" + role);

        // Використовуємо різні методи в залежності від комбінації фільтрів
        try {
            if (username != null && method != null && role != null) {
                System.out.println("📋 Using filter: username + method + role");
                return auditRepository.findByUsernameContainingIgnoreCaseAndMethodAndRoleOrderByTimestampDesc(username, method, role, pageable);
            } else if (username != null && method != null) {
                System.out.println("📋 Using filter: username + method");
                return auditRepository.findByUsernameContainingIgnoreCaseAndMethodOrderByTimestampDesc(username, method, pageable);
            } else if (username != null && role != null) {
                System.out.println("📋 Using filter: username + role");
                return auditRepository.findByUsernameContainingIgnoreCaseAndRoleOrderByTimestampDesc(username, role, pageable);
            } else if (method != null && role != null) {
                System.out.println("📋 Using filter: method + role");
                return auditRepository.findByMethodAndRoleOrderByTimestampDesc(method, role, pageable);
            } else if (username != null) {
                System.out.println("📋 Using filter: username only");
                return auditRepository.findByUsernameContainingIgnoreCaseOrderByTimestampDesc(username, pageable);
            } else if (method != null) {
                System.out.println("📋 Using filter: method only");
                return auditRepository.findByMethodOrderByTimestampDesc(method, pageable);
            } else if (role != null) {
                System.out.println("📋 Using filter: role only");
                return auditRepository.findByRoleOrderByTimestampDesc(role, pageable);
            } else {
                System.out.println("📋 No filters - returning all logs");
                return auditRepository.findAll(pageable);
            }
        } catch (Exception e) {
            System.err.println("❌ Error filtering logs: " + e.getMessage());
            e.printStackTrace();
            // Якщо помилка - повертаємо всі логи
            return auditRepository.findAll(pageable);
        }
    }


    public long getUniqueUsersCount() {
        return auditRepository.countDistinctUsername();
    }

    public long getUniqueIPsCount() {
        return auditRepository.countDistinctClientIp();
    }

    public long getTodayLogsCount() {
        return auditRepository.countByTimestampAfter(LocalDate.now().atStartOfDay());
    }

}

