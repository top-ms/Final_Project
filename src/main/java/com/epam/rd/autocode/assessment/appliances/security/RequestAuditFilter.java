package com.epam.rd.autocode.assessment.appliances.security;

import com.epam.rd.autocode.assessment.appliances.service.JwtService;
import com.epam.rd.autocode.assessment.appliances.service.impl.RequestAuditService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

import org.slf4j.Logger;

@Component
@Order(1) // Виконується перед JWT фільтром
public class RequestAuditFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(RequestAuditFilter.class);

    private final JwtService jwtService;
    private final RequestAuditService auditService;

    public RequestAuditFilter(JwtService jwtService, RequestAuditService auditService) {
        this.jwtService = jwtService;
        this.auditService = auditService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath();
        String method = request.getMethod();
        String clientIp = getClientIpAddress(request);
        String userAgent = request.getHeader("User-Agent");

        // Витягуємо JWT токен
        String jwtToken = extractJwtFromCookies(request);

        String username = "ANONYMOUS";
        String userId = null;
        String role = null;

        if (jwtToken != null && !isAuthEndpoint(path)) {
            try {
                // 📊 Витягуємо дані користувача з токена
                username = jwtService.extractUsername(jwtToken);
                userId = jwtService.extractUserId(jwtToken);
                role = jwtService.extractRole(jwtToken);

                log.info("🔍 REQUEST AUDIT: User {} (ID: {}, Role: {}) {} {} from IP: {}",
                        username, userId, role, method, path, clientIp);

            } catch (Exception e) {
                log.warn("❌ Could not extract user info from JWT: {}", e.getMessage());
                username = "INVALID_TOKEN";
            }
        } else {
            // Анонімний користувач або auth endpoints
            log.info("👤 ANONYMOUS REQUEST: {} {} from IP: {}", method, path, clientIp);
        }

        // Зберігаємо інформацію в базу даних (асинхронно)
        auditService.logRequestAsync(username, userId, role, path, method, clientIp, userAgent);

        // Продовжуємо обробку запиту
        filterChain.doFilter(request, response);
    }

    private String extractJwtFromCookies(HttpServletRequest request) {
        if (request.getCookies() != null) {
            return Arrays.stream(request.getCookies())
                    .filter(cookie -> "jwt".equals(cookie.getName()))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElse(null);
        }
        return null;
    }

    private boolean isAuthEndpoint(String path) {
        return "/login".equals(path) || "/register".equals(path) ||
                path.startsWith("/h2-console") || path.startsWith("/css") ||
                path.startsWith("/js") || path.startsWith("/images");
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }

        return request.getRemoteAddr();
    }
}
