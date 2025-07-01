package com.epam.rd.autocode.assessment.appliances.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import jakarta.persistence.Entity;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Admin extends User {

    public Admin(Long id, String name, String email, String password) {
        super(id, name, email, password);
    }
}
