package com.epam.rd.autocode.assessment.appliances.dto.orderDTO;

import java.math.BigDecimal;

public class ViewOrdersDTO {
    private Long id;
    private String clientName;
    private String clientEmail;
    private String employeeName;
    private String employeeEmail;
    private BigDecimal price;
    private Boolean approved;

    // Constructors
    public ViewOrdersDTO() {
    }

    public ViewOrdersDTO(Long id, String clientName, String clientEmail, String employeeName, String employeeEmail, BigDecimal price, Boolean approved) {
        this.id = id;
        this.clientName = clientName;
        this.clientEmail = clientEmail;
        this.employeeName = employeeName;
        this.employeeEmail = employeeEmail;
        this.price = price;
        this.approved = approved;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public void setEmployeeEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }
}