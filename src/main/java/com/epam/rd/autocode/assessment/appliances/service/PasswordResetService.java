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

    private final PasswordResetTokenRepository tokenRepository;
    private final ClientService clientService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;



    @Value("${app.password-reset.token-expiry-hours:1}")
    private int tokenExpiryHours;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    public PasswordResetService(PasswordResetTokenRepository tokenRepository, ClientService clientService, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.tokenRepository = tokenRepository;
        this.clientService = clientService;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public boolean createPasswordResetToken(String email) {
        Optional<Client> clientOptional = clientService.findClientByEmail(email);
        if (clientOptional.isEmpty()) {
            return false;
        }
        tokenRepository.deactivateAllTokensForEmail(email);
        String token = UUID.randomUUID().toString();
        LocalDateTime expiryDate = LocalDateTime.now().plusHours(tokenExpiryHours);
        PasswordResetToken resetToken = new PasswordResetToken(email, token, expiryDate);
        tokenRepository.save(resetToken);
        String resetLink = baseUrl + "/reset-password?token=" + token;
        emailService.sendPasswordResetEmail(email, resetLink, clientOptional.get().getName());
        return true;
    }

    public boolean isValidToken(String token) {
        Optional<PasswordResetToken> tokenOptional = tokenRepository.findByToken(token);
        return tokenOptional.isPresent() && tokenOptional.get().isValid();
    }

    public Optional<String> getEmailByToken(String token) {
        Optional<PasswordResetToken> tokenOptional = tokenRepository.findByToken(token);
        if (tokenOptional.isPresent() && tokenOptional.get().isValid()) {
            return Optional.of(tokenOptional.get().getEmail());
        }
        return Optional.empty();
    }

    @Transactional
    public boolean resetPassword(String token, String newPassword) {
        Optional<PasswordResetToken> tokenOptional = tokenRepository.findByToken(token);
        if (tokenOptional.isEmpty() || !tokenOptional.get().isValid()) {
            return false;
        }
        PasswordResetToken resetToken = tokenOptional.get();
        String email = resetToken.getEmail();
        Optional<Client> clientOptional = clientService.findClientByEmail(email);
        if (clientOptional.isEmpty()) {
            return false;
        }
        Client client = clientOptional.get();
        String encodedPassword = passwordEncoder.encode(newPassword);
        boolean passwordUpdated = clientService.updateClientPassword(client.getId(), encodedPassword);
        if (passwordUpdated) {
            resetToken.setIsUsed(true);
            tokenRepository.save(resetToken);
            tokenRepository.deactivateAllTokensForEmail(email);
            return true;
        }
        return false;
    }

    public boolean hasActiveToken(String email) {
        return tokenRepository.existsActiveTokenByEmail(email, LocalDateTime.now());
    }

    @Transactional
    public void cleanupExpiredTokens() {
        tokenRepository.deleteExpiredTokens(LocalDateTime.now());
    }
}