package com.epam.rd.autocode.assessment.appliances.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Objects;

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

    @ManyToOne
    @JoinColumn(name = "orders_id", nullable = false)
    private Orders order;

    // Безаргументний конструктор
    public OrderRow() {
    }



    // Конструктор для тестів (4 параметри)
    public OrderRow(Long id, Appliance appliance, Long number, BigDecimal amount) {
        this.id = id;
        this.appliance = appliance;
        this.number = number;
        this.amount = amount;
    }

    // Getters and Setters


    public void setId(Long id) {
        this.id = id;
    }

    public void setAppliance(Appliance appliance) {
        this.appliance = appliance;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setOrder(Orders order) {
        this.order = order;
    }

    public Long getId() {
        return id;
    }

    public Appliance getAppliance() {
        return appliance;
    }

    public Orders getOrder() {
        return order;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Long getNumber() {
        return number;
    }




    // equals and hashCode

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderRow)) return false;
        OrderRow orderRow = (OrderRow) o;
        return Objects.equals(id, orderRow.id) &&
                Objects.equals(appliance, orderRow.appliance) &&
                Objects.equals(number, orderRow.number) &&
                Objects.equals(amount, orderRow.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, appliance, number, amount);
    }

    // toString

    @Override
    public String toString() {
        return "OrderRow{" +
                "id=" + id +
                ", appliance=" + appliance +
                ", number=" + number +
                ", amount=" + amount +
                '}';
    }
}