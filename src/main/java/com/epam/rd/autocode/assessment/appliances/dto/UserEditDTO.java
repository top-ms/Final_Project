package com.epam.rd.autocode.assessment.appliances.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEditDTO {

    private Long id;

    @NotBlank(message = "Ім'я не може бути порожнім")
    @Size(min = 2, max = 50, message = "Ім'я має бути від 2 до 50 символів")
    private String name;

    @NotBlank(message = "Email не може бути порожнім")
    @Email(message = "Введіть коректний email")
    private String email;

    @NotBlank(message = "Пароль не може бути порожнім")
    @Size(min = 6, max = 100, message = "Пароль має бути від 6 до 100 символів")
    private String password;
}
