package com.epam.rd.autocode.assessment.appliances.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "request_audit_log")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestAuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "role")
    private String role;

    @Column(name = "request_path")
    private String path;

    @Column(name = "http_method")
    private String method;

    @Column(name = "client_ip")
    private String clientIp;

    @Column(name = "user_agent", length = 500)
    private String userAgent;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Column(name = "response_status")
    private Integer responseStatus; // можна додати пізніше
}

