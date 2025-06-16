package com.epam.rd.autocode.assessment.appliances.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
//@Table(name = "orders") // щоб уникнути конфлікту з SQL-запитами (orders — зарезервоване слово)
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




    @OneToMany(mappedBy = "id", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderRow> orderRowSet = new LinkedHashSet<>();




    @Column(nullable = false)
    private Boolean approved;

    // Конструктор для тестів (5 параметрів)
    public Orders(Long id, Client client, Employee employee, Set<OrderRow> orderRowSet, Boolean approved) {
        this.id = id;
        this.client = client;
        this.employee = employee;
        this.orderRowSet = orderRowSet;
        this.approved = approved;
    }
}
