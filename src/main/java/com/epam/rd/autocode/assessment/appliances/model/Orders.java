package com.epam.rd.autocode.assessment.appliances.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"orderRowSet", "employee", "client"})
@ToString(exclude = {"orderRowSet"})
public class Orders {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderRow> orderRowSet = new LinkedHashSet<>();

    @Column(nullable = false)
    private Boolean approved;



    @Transient
    public BigDecimal getAmount() {
        if (orderRowSet == null || orderRowSet.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return orderRowSet.stream()
                .map(OrderRow::getAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}