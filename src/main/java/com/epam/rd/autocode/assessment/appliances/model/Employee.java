package com.epam.rd.autocode.assessment.appliances.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Employee extends User {

    @Column(nullable = false)
    private String department;

    public Employee(Long id, String name, String email, String password, String department) {
        super(id, name, email, password);
        this.department = department;
    }

    public String getDepartment() {
        return department;
    }
}
