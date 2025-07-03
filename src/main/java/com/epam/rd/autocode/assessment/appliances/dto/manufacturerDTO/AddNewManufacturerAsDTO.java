package com.epam.rd.autocode.assessment.appliances.dto.manufacturerDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Valid
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddNewManufacturerAsDTO {
    @NotBlank(message = "{user.name.is.mandatory}")
    @Pattern(
            regexp = "^[A-Za-zА-Яа-яІіЇїЄєґҐ'’\\-\\s]+$",
            message = "{validation.name.letters.only}"
    )
    String name;
}
