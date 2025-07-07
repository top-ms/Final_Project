package com.epam.rd.autocode.assessment.appliances.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.logging.Logger;

@Service
public class EmailService {

    private static final Logger logger = Logger.getLogger(EmailService.class.getName());

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.name:Appliances Store}")
    private String appName;

    /**
     * Відправити email для скидання пароля
     */
    public boolean sendPasswordResetEmail(String toEmail, String resetLink, String clientName) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            // Налаштування повідомлення
            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Скидання пароля - " + appName);

            // Створюємо контекст для Thymeleaf
            Context context = new Context();
            context.setVariable("clientName", clientName);
            context.setVariable("resetLink", resetLink);
            context.setVariable("appName", appName);

            // Генеруємо HTML контент з шаблону
            String htmlContent = templateEngine.process("email/password-reset", context);
            helper.setText(htmlContent, true);

            // Відправляємо
            mailSender.send(message);

            logger.info("Password reset email sent successfully to: " + toEmail);
            return true;

        } catch (MessagingException | MailException e) {
            logger.severe("Failed to send password reset email to " + toEmail + ": " + e.getMessage());
            return false;
        }
    }

    /**
     * Відправити простий текстовий email (fallback)
     */
    public boolean sendSimplePasswordResetEmail(String toEmail, String resetLink, String clientName) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Скидання пароля - " + appName);

            String textContent = String.format(
                    "Вітаємо, %s!\n\n" +
                            "Ви запросили скидання пароля для вашого акаунту в %s.\n\n" +
                            "Щоб встановити новий пароль, перейдіть за посиланням:\n%s\n\n" +
                            "Посилання дійсне протягом 1 години.\n\n" +
                            "Якщо ви не запитували скидання пароля, проігноруйте цей лист.\n\n" +
                            "З повагою,\nКоманда %s",
                    clientName, appName, resetLink, appName
            );

            helper.setText(textContent, false);
            mailSender.send(message);

            logger.info("Simple password reset email sent successfully to: " + toEmail);
            return true;

        } catch (MessagingException | MailException e) {
            logger.severe("Failed to send simple password reset email to " + toEmail + ": " + e.getMessage());
            return false;
        }
    }
}