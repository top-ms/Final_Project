package com.epam.rd.autocode.assessment.appliances.dto.applianceDTO;

import com.epam.rd.autocode.assessment.appliances.model.Category;
import com.epam.rd.autocode.assessment.appliances.model.Manufacturer;
import com.epam.rd.autocode.assessment.appliances.model.PowerType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditApplianceDTO {

    private Long id;

    private String name;

    private Category category;

    private String model;

    private Manufacturer manufacturer;

    private PowerType powerType;

    private String characteristic;

    private String description;

    private Integer power;

    private BigDecimal price;
}
