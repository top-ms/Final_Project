package com.epam.rd.autocode.assessment.appliances.model;

import jakarta.persistence.Entity;

@Entity
public class Admin extends User{

    public Admin() {
    }

    public Admin(Long id, String name, String email, String password) {
        this.setId(id);
        this.setName(name);
        this.setEmail(email);
        this.setPassword(password);
    }

    @Override
    public String toString() {
        return "Admin{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", password='" + getPassword() + '\'' +
                '}';
    }
}
