package com.epam.rd.autocode.assessment.appliances.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Entity
public class OrderRow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "appliance_id", nullable = false)
    private Appliance appliance;

    @Column(nullable = false)
    private Long number;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    // Конструктор для тестів (4 параметри)
    public OrderRow(Long id, Appliance appliance, Long number, BigDecimal amount) {
        this.id = id;
        this.appliance = appliance;
        this.number = number;
        this.amount = amount;
    }
}
