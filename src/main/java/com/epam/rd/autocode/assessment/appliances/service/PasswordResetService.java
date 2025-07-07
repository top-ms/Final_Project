package com.epam.rd.autocode.assessment.appliances.service;

import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.model.PasswordResetToken;
import com.epam.rd.autocode.assessment.appliances.repository.PasswordResetTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetService {

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private ClientService clientService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${app.password-reset.token-expiry-hours:1}")
    private int tokenExpiryHours;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    /**
     * Створити токен для скидання пароля і відправити email
     */
    @Transactional
    public boolean createPasswordResetToken(String email) {
        // Перевіряємо чи існує клієнт з таким email
        Optional<Client> clientOptional = clientService.findClientByEmail(email);
        if (clientOptional.isEmpty()) {
            return false; // Клієнт не знайдений
        }

        // Деактивуємо всі попередні токени для цього email
        tokenRepository.deactivateAllTokensForEmail(email);

        // Генеруємо новий токен
        String token = UUID.randomUUID().toString();
        LocalDateTime expiryDate = LocalDateTime.now().plusHours(tokenExpiryHours);

        // Зберігаємо токен
        PasswordResetToken resetToken = new PasswordResetToken(email, token, expiryDate);
        tokenRepository.save(resetToken);

        // Відправляємо email
        String resetLink = baseUrl + "/reset-password?token=" + token;
        boolean emailSent = emailService.sendPasswordResetEmail(email, resetLink, clientOptional.get().getName());

        return emailSent;
    }

    /**
     * Перевірити чи токен валідний
     */
    public boolean isValidToken(String token) {
        Optional<PasswordResetToken> tokenOptional = tokenRepository.findByToken(token);
        return tokenOptional.isPresent() && tokenOptional.get().isValid();
    }

    /**
     * Отримати email по токену
     */
    public Optional<String> getEmailByToken(String token) {
        Optional<PasswordResetToken> tokenOptional = tokenRepository.findByToken(token);
        if (tokenOptional.isPresent() && tokenOptional.get().isValid()) {
            return Optional.of(tokenOptional.get().getEmail());
        }
        return Optional.empty();
    }

    /**
     * Змінити пароль за токеном
     */
    @Transactional
    public boolean resetPassword(String token, String newPassword) {
        Optional<PasswordResetToken> tokenOptional = tokenRepository.findByToken(token);

        if (tokenOptional.isEmpty() || !tokenOptional.get().isValid()) {
            return false;
        }

        PasswordResetToken resetToken = tokenOptional.get();
        String email = resetToken.getEmail();

        // Знаходимо клієнта
        Optional<Client> clientOptional = clientService.findClientByEmail(email);
        if (clientOptional.isEmpty()) {
            return false;
        }

        // Оновлюємо пароль
        Client client = clientOptional.get();
        String encodedPassword = passwordEncoder.encode(newPassword);

        boolean passwordUpdated = clientService.updateClientPassword(client.getId(), encodedPassword);

        if (passwordUpdated) {
            // Позначаємо токен як використаний
            resetToken.setIsUsed(true);
            tokenRepository.save(resetToken);

            // Деактивуємо всі інші токени для цього email
            tokenRepository.deactivateAllTokensForEmail(email);

            return true;
        }

        return false;
    }

    /**
     * Перевірити чи є активний токен для email
     */
    public boolean hasActiveToken(String email) {
        return tokenRepository.existsActiveTokenByEmail(email, LocalDateTime.now());
    }

    /**
     * Очистити застарілі токени (можна викликати по розкладу)
     */
    @Transactional
    public void cleanupExpiredTokens() {
        tokenRepository.deleteExpiredTokens(LocalDateTime.now());
    }
}