package com.epam.rd.autocode.assessment.appliances.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
//@Table(name = "orders") // якщо потрібна назва таблиці — розкоментуй
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

    


    // Безаргументний конструктор
    public Orders() {
    }

    // Конструктор для тестів (або ініціалізації)
    public Orders(Long id, Client client, Employee employee, Set<OrderRow> orderRowSet, Boolean approved) {
        this.id = id;
        this.client = client;
        this.employee = employee;
        this.orderRowSet = orderRowSet;
        this.approved = approved;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Set<OrderRow> getOrderRowSet() {
        return orderRowSet;
    }

    public void setOrderRowSet(Set<OrderRow> orderRowSet) {
        this.orderRowSet = orderRowSet;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    // equals and hashCode

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Orders)) return false;
        Orders orders = (Orders) o;
        return Objects.equals(id, orders.id)
                && Objects.equals(employee, orders.employee)
                && Objects.equals(client, orders.client)
                && Objects.equals(orderRowSet, orders.orderRowSet)
                && Objects.equals(approved, orders.approved);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, employee, client, orderRowSet, approved);
    }


    // toString

    @Override
    public String toString() {
        return "Orders{" +
                "id=" + id +
                ", employee=" + employee +
                ", client=" + client +
                ", orderRowSet=" + orderRowSet +
                ", approved=" + approved +
                '}';
    }


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