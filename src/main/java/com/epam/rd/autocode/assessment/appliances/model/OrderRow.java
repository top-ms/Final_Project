package com.epam.rd.autocode.assessment.appliances.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"order", "appliance"})
@ToString(exclude = {"order"})
public class OrderRow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "appliance_id", nullable = false)
    private Appliance appliance;

    @Column(nullable = false)
    private Long number;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orders_id", nullable = false)
    private Orders order;

    public OrderRow(Long id, Appliance appliance, Long number, BigDecimal amount) {
        this.id = id;
        this.appliance = appliance;
        this.number = number;
        this.amount = amount;
    }
}