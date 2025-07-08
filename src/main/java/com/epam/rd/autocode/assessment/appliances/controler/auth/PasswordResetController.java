package com.epam.rd.autocode.assessment.appliances.controler.auth;

import com.epam.rd.autocode.assessment.appliances.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Optional;

@Controller
public class PasswordResetController {

    @Autowired
    private PasswordResetService passwordResetService;

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "auth/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String email,
                                        RedirectAttributes redirectAttributes) {
        if (email == null || email.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Будь ласка, введіть email адресу");
            return "redirect:/forgot-password";
        }
        email = email.trim().toLowerCase();
        if (!email.contains("@") || !email.contains(".")) {
            redirectAttributes.addFlashAttribute("error", "Неправильний формат email адреси");
            return "redirect:/forgot-password";
        }
        if (passwordResetService.hasActiveToken(email)) {
            redirectAttributes.addFlashAttribute("warning",
                    "Запит на скидання пароля вже відправлено. Перевірте пошту або спробуйте пізніше.");
            return "redirect:/forgot-password";
        }
        boolean success = passwordResetService.createPasswordResetToken(email);
        if (success) {
            redirectAttributes.addFlashAttribute("success",
                    "Інструкції для скидання пароля відправлено на вашу пошту. Перевірте папку 'Спам' якщо не знайдете лист.");
        } else {
            redirectAttributes.addFlashAttribute("error",
                    "Клієнт з таким email не знайдений або сталася помилка при відправці листа");
        }
        return "redirect:/forgot-password";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {

        if (token == null || token.trim().isEmpty()) {
            model.addAttribute("error", "Невірне посилання для скидання пароля");
            return "auth/reset-password-error";
        }
        if (!passwordResetService.isValidToken(token)) {
            model.addAttribute("error", "Посилання недійсне або застаріло. Спробуйте запросити скидання пароля ще раз.");
            return "auth/reset-password-error";
        }
        Optional<String> emailOptional = passwordResetService.getEmailByToken(token);
        if (emailOptional.isPresent()) {
            model.addAttribute("email", emailOptional.get());
        }
        model.addAttribute("token", token);
        return "auth/reset-password-form";
    }

    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam("token") String token,
                                       @RequestParam("newPassword") String newPassword,
                                       @RequestParam("confirmPassword") String confirmPassword,
                                       RedirectAttributes redirectAttributes) {
        if (token == null || token.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Невірний токен");
            return "redirect:/login";
        }
        if (!passwordResetService.isValidToken(token)) {
            redirectAttributes.addFlashAttribute("error", "Токен недійсний або застарів");
            return "redirect:/forgot-password";
        }
        if (newPassword == null || newPassword.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Пароль не може бути порожнім");
            return "redirect:/reset-password?token=" + token;
        }
        if (newPassword.length() < 6) {
            redirectAttributes.addFlashAttribute("error", "Пароль повинен містити принаймні 6 символів");
            return "redirect:/reset-password?token=" + token;
        }
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Паролі не співпадають");
            return "redirect:/reset-password?token=" + token;
        }

        boolean success = passwordResetService.resetPassword(token, newPassword);
        if (success) {
            redirectAttributes.addFlashAttribute("success",
                    "Пароль успішно змінено! Тепер ви можете увійти з новим паролем.");
            return "redirect:/login";
        } else {
            redirectAttributes.addFlashAttribute("error",
                    "Сталася помилка при зміні пароля. Спробуйте ще раз.");
            return "redirect:/reset-password?token=" + token;
        }
    }

    @GetMapping("/reset-password-error")
    public String showResetPasswordError() {
        return "auth/reset-password-error";
    }
}