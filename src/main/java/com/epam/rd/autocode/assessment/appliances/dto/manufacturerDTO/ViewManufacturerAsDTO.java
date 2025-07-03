package com.epam.rd.autocode.assessment.appliances.dto.manufacturerDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViewManufacturerAsDTO {

    Long id;

    String name;
}
