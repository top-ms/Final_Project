package com.epam.rd.autocode.assessment.appliances.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Employee extends User {

    @Column(nullable = false)
    private String department;

    public Employee() {
    }

    public Employee(Long id, String name, String email, String password, String department) {
        this.setId(id);
        this.setName(name);
        this.setEmail(email);
        this.setPassword(password);
        this.department = department;
    }

    // Геттер і сетер для department
    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    // toString (через геттери)
    @Override
    public String toString() {
        return "Employee{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", password='" + getPassword() + '\'' +
                ", department='" + department + '\'' +
                '}';
    }
}