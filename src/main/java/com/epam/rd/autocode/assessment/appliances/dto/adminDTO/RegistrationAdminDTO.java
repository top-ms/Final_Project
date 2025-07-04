package com.epam.rd.autocode.assessment.appliances.dto.adminDTO;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

@Valid
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationAdminDTO {

    @NotBlank(message = "{user.name.is.mandatory}")
    @Pattern(
            regexp = "^[A-Za-zА-Яа-яІіЇїЄєґҐ'’\\-\\s]+$",
            message = "{validation.name.letters.only}"
    )
    private String name;

    @NotBlank(message = "{user.email.is.blank}")
    @Email(message = "{user.email.is.correctly}")
    private String email;

    @NotBlank(message = "{user.password.is.blank}")
    @Size(min = 6, max = 30, message = "{user.password.length}")
    private String password;
}

