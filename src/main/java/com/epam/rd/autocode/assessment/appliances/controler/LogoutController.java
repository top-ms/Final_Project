package com.epam.rd.autocode.assessment.appliances.controler;

import com.epam.rd.autocode.assessment.appliances.service.JwtService;
import com.epam.rd.autocode.assessment.appliances.service.impl.RequestAuditService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Arrays;


@Controller
public class LogoutController {

    private static final Logger log = LoggerFactory.getLogger(LogoutController.class);

    private final JwtService jwtService;
    private final RequestAuditService auditService;

    public LogoutController(JwtService jwtService, RequestAuditService auditService) {
        this.jwtService = jwtService;
        this.auditService = auditService;
    }

    // 🚪 POST logout (основний метод)
    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        String username = "UNKNOWN";
        String userId = null;
        String role = null;

        try {
            // 🔍 Витягуємо інформацію з JWT перед видаленням
            String jwtToken = extractJwtFromCookies(request);
            if (jwtToken != null && jwtService.isValidJwtFormat(jwtToken)) {
                try {
                    username = jwtService.extractUsername(jwtToken);
                    userId = jwtService.extractUserId(jwtToken);
                    role = jwtService.extractRole(jwtToken);

                    log.info("🔍 Logout attempt from user: {} (ID: {}, Role: {})", username, userId, role);
                } catch (Exception e) {
                    log.warn("⚠️ Could not extract user info from JWT during logout: {}", e.getMessage());
                    username = "INVALID_TOKEN_USER";
                }
            }

            // 🗑️ Видаляємо JWT cookie
            Cookie jwtCookie = new Cookie("jwt", null);
            jwtCookie.setHttpOnly(true); // Захист від JavaScript
            jwtCookie.setSecure(false); // Поставте true для HTTPS
            jwtCookie.setPath("/"); // Доступний для всього сайту
            jwtCookie.setMaxAge(0); // Видаляємо cookie (встановлюємо час життя = 0)

            response.addCookie(jwtCookie);

            // 🧹 Очищуємо Security Context (Spring Security)
            SecurityContextHolder.clearContext();

            // 📊 Логуємо logout в аудит систему
            String clientIp = getClientIpAddress(request);
            String userAgent = request.getHeader("User-Agent");

            auditService.logRequestAsync(username, userId, role, "/logout", "POST", clientIp, userAgent);

            log.info("👋 User {} successfully logged out from IP: {}", username, clientIp);

        } catch (Exception e) {
            log.error("❌ Error during logout process: {}", e.getMessage(), e);
            // Навіть якщо помилка - все одно виконуємо logout
        }

        // 🔄 Перенаправляємо на сторінку логіну з повідомленням про успішний вихід
        return "redirect:/login?logout=success";
    }

    // 🚪 GET logout (для посилань типу <a href="/logout">)
    @GetMapping("/logout")
    public String logoutGet(HttpServletRequest request, HttpServletResponse response) {
        log.debug("🔗 GET logout request received, redirecting to POST logout logic");
        return logout(request, response);
    }

    // 🍪 Витягуємо JWT токен з cookies
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

    // 🌐 Отримуємо реальний IP адрес клієнта (враховуємо проксі)
    private String getClientIpAddress(HttpServletRequest request) {
        // Перевіряємо заголовки проксі серверів
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            // Беремо перший IP (якщо їх кілька через кому)
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }

        String xForwarded = request.getHeader("X-Forwarded");
        if (xForwarded != null && !xForwarded.isEmpty() && !"unknown".equalsIgnoreCase(xForwarded)) {
            return xForwarded;
        }

        String forwarded = request.getHeader("Forwarded");
        if (forwarded != null && !forwarded.isEmpty() && !"unknown".equalsIgnoreCase(forwarded)) {
            return forwarded;
        }

        // Якщо немає проксі - повертаємо звичайний remote address
        return request.getRemoteAddr();
    }
}
