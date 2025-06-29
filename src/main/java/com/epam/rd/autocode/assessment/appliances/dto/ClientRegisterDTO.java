package com.epam.rd.autocode.assessment.appliances.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ClientRegisterDTO {

    @NotBlank(message = "{user.name.is.mandatory}")
    private String name;

    @NotBlank(message = "{user.email.is.blank}")
    @Email(message = "{user.email.is.correctly}")
    private String email;

    @NotBlank(message = "{user.password.is.blank}")
    @Size(min = 6, max = 30, message = "{user.password.length}")
    private String password;

    public ClientRegisterDTO() {
    }

    public ClientRegisterDTO(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}