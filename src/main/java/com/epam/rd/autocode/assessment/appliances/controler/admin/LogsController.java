package com.epam.rd.autocode.assessment.appliances.controler.admin;

import com.epam.rd.autocode.assessment.appliances.model.RequestAuditLog;
import com.epam.rd.autocode.assessment.appliances.service.impl.RequestAuditService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;

@Controller
@RequestMapping("/admin")
public class LogsController {

    private final RequestAuditService auditService;

    public LogsController(RequestAuditService auditService) {
        this.auditService = auditService;
    }

    @GetMapping("/logs")
    public String viewLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String method,
            @RequestParam(required = false) String role,
            Model model) {

        System.out.println("🔍 Logs request received:");
        System.out.println("📄 Page: " + page + ", Size: " + size);
        System.out.println("📋 Filters - Username: '" + username + "', Method: '" + method + "', Role: '" + role + "'");

        try {
            // Отримуємо логи з пагінацією та фільтрами
            Page<RequestAuditLog> logsPage = auditService.getFilteredLogs(page, size, username, method, role);

            System.out.println("📊 Query results: " + logsPage.getTotalElements() + " total logs found");
            System.out.println("📋 Current page content: " + logsPage.getContent().size() + " logs");

            // Основні дані для таблиці
            model.addAttribute("logs", logsPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", logsPage.getTotalPages());
            model.addAttribute("totalLogs", logsPage.getTotalElements());

            // Зберігаємо поточні значення фільтрів для форми
            model.addAttribute("currentUsername", username);
            model.addAttribute("currentMethod", method);
            model.addAttribute("currentRole", role);

            // Додаткова статистика
            long uniqueUsers = auditService.getUniqueUsersCount();
            long uniqueIPs = auditService.getUniqueIPsCount();
            long todayLogs = auditService.getTodayLogsCount();

            model.addAttribute("uniqueUsers", uniqueUsers);
            model.addAttribute("uniqueIPs", uniqueIPs);
            model.addAttribute("todayLogs", todayLogs);

            System.out.println("📈 Statistics - Users: " + uniqueUsers + ", IPs: " + uniqueIPs + ", Today: " + todayLogs);

            // Додаємо інформацію про наявність фільтрів
            boolean hasFilters = (username != null && !username.trim().isEmpty()) ||
                    (method != null && !method.trim().isEmpty()) ||
                    (role != null && !role.trim().isEmpty());
            model.addAttribute("hasActiveFilters", hasFilters);

            // Додаємо інформацію про пагінацію
            model.addAttribute("hasPrevious", page > 0);
            model.addAttribute("hasNext", page < logsPage.getTotalPages() - 1);
            model.addAttribute("isFirstPage", page == 0);
            model.addAttribute("isLastPage", page >= logsPage.getTotalPages() - 1);

            // Для debug - додаємо перші кілька записів в лог
            if (!logsPage.getContent().isEmpty()) {
                RequestAuditLog firstLog = logsPage.getContent().get(0);
                System.out.println("🔍 First log: " + firstLog.getUsername() + " -> " + firstLog.getPath() + " at " + firstLog.getTimestamp());
            }

        } catch (Exception e) {
            System.err.println("❌ Error loading logs page: " + e.getMessage());
            e.printStackTrace();

            // Додаємо порожні значення у випадку помилки
            model.addAttribute("logs", new ArrayList<>());
            model.addAttribute("currentPage", 0);
            model.addAttribute("totalPages", 0);
            model.addAttribute("totalLogs", 0L);
            model.addAttribute("uniqueUsers", 0L);
            model.addAttribute("uniqueIPs", 0L);
            model.addAttribute("todayLogs", 0L);
            model.addAttribute("hasActiveFilters", false);
            model.addAttribute("hasPrevious", false);
            model.addAttribute("hasNext", false);
            model.addAttribute("isFirstPage", true);
            model.addAttribute("isLastPage", true);

            // Зберігаємо параметри навіть при помилці
            model.addAttribute("currentUsername", username);
            model.addAttribute("currentMethod", method);
            model.addAttribute("currentRole", role);

            // Додаємо повідомлення про помилку
            model.addAttribute("errorMessage", "Помилка завантаження логів: " + e.getMessage());
        }

        return "admin/viewLogs"; // Назва Thymeleaf шаблону
    }

}

