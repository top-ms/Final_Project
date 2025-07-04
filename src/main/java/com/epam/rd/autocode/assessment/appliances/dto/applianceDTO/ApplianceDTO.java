package com.epam.rd.autocode.assessment.appliances.dto.applianceDTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplianceDTO {


    private Long id;

    private String name;

    private String category;

    private String model;

    private String manufacturer;

    private String powerType;

    private String characteristic;

    private String description;

    private Integer power;

    private BigDecimal price;
}